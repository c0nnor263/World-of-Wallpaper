package com.doodle.core.billing.data

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.acknowledgePurchase
import com.android.billingclient.api.consumePurchase
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchaseHistory
import com.android.billingclient.api.queryPurchasesAsync
import com.doodle.core.billing.domain.enums.BillingProductType
import com.doodle.core.billing.domain.enums.VerifyResult
import com.doodle.core.billing.domain.model.ProductDetailsInfo
import com.doodle.core.billing.domain.model.PurchaseProduct
import com.doodle.core.domain.di.ApplicationScope
import com.doodle.core.domain.di.IoDispatcher
import com.doodle.core.domain.enums.RemoveAdsStatus
import com.doodle.core.domain.source.local.repository.UserPreferencesDataStoreRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ActivityRetainedScoped
class BillingDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    @ApplicationScope private val scope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val userPreferencesDataStoreRepository: UserPreferencesDataStoreRepository
) {
    private var initialBillingStartTime = 0L
    var fetchDelay: Long = TimeUnit.SECONDS.toMillis(1)

    private val _productsDetailsFlow = MutableStateFlow(ProductDetailsInfo())
    val productsDetailsFlow: StateFlow<ProductDetailsInfo> = _productsDetailsFlow.asStateFlow()

    private val _pendingBenefitFlow = MutableStateFlow(PurchaseProduct())
    val pendingBenefitFlow: StateFlow<PurchaseProduct> = _pendingBenefitFlow.asStateFlow()

    private val billingStateListener = object : BillingClientStateListener {
        override fun onBillingSetupFinished(billingResult: BillingResult) {
            initialBillingStartTime = System.currentTimeMillis()
            Log.i(
                "TAG",
                "onBillingSetupFinished: ${billingResult.responseCode} ${billingResult.debugMessage}"
            )
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                scope.launch {
                    queryProductDetails()
                }
            }
        }

        override fun onBillingServiceDisconnected() {
            scope.launch {
                delay(fetchDelay)
                initClient()

                val timePassFromClientStarted = System.currentTimeMillis() - initialBillingStartTime
                if (timePassFromClientStarted > TimeUnit.SECONDS.toMillis(30)) {
                    fetchDelay = TimeUnit.SECONDS.toMillis(1)
                } else {
                    fetchDelay += fetchDelay
                }
            }
        }
    }
    private val purchasesListener = PurchasesUpdatedListener { billingResult, purchases ->
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { purchase ->
                    if (
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                        purchase.isAcknowledged.not()
                    ) {
                        verifyPurchase(purchase)
                    }
                }
            }

            BillingClient.BillingResponseCode.USER_CANCELED -> {
                updatePendingBenefitResult(VerifyResult.CANCELLED)
            }

            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> {
                initClient()
            }

            else -> {}
        }
    }

    private val client =
        BillingClient.newBuilder(context)
            .setListener(purchasesListener)
            .enablePendingPurchases()
            .build()

    fun initClient() {
        client.startConnection(billingStateListener)
    }

    fun endConnection() {
        client.endConnection()
    }

    fun onResumeBilling() {
        if (client.isReady) {
            scope.launch {
                queryProductDetails()
            }
        }
    }

    suspend fun queryProductDetails() = withContext(ioDispatcher) {
        val inAppParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                BillingProductType.entries
                    .filter { it.productType == ProductType.INAPP }
                    .map {
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(it.id)
                            .setProductType(ProductType.INAPP)
                            .build()
                    }
            )
            .build()

        val (billingResult, productDetailsList) = client.queryProductDetails(inAppParams)

        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            _productsDetailsFlow.value = _productsDetailsFlow.value.copy(
                inAppDetails = productDetailsList?.toImmutableList()
            )
        }
    }

    private suspend fun queryPurchases() = withContext(ioDispatcher) {
        val params =
            QueryPurchasesParams
                .newBuilder()
                .setProductType(ProductType.INAPP)
                .build()

        val (billingResult, purchasesList) = client.queryPurchasesAsync(params)

        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            purchasesList.forEach { purchase ->
                verifyPurchase(purchase)
            }
        }
    }

    private fun verifyPurchase(purchase: Purchase?) {
        val purchaseJson = purchase?.originalJson?.let { JSONObject(it) }
        val productId = purchaseJson?.getString("productId")

        updatePendingBenefitType(purchaseJson)
        when (productId) {
            BillingProductType.REMOVE_ADS.id -> {
                acknowledgePurchase(purchase)
            }

            else -> consumePurchase(purchase)
        }
    }

    suspend fun purchaseProduct(
        type: BillingProductType,
        activity: ComponentActivity,
        onError: () -> Unit
    ) {

        val details = when (type) {
            BillingProductType.REMOVE_ADS -> {
                productsDetailsFlow.firstOrNull()?.inAppDetails?.filter { it.productId == type.id }
                    ?.getOrNull(0)
            }
        }

        if (details == null) {
            onError()
            return
        }

        try {
            val productDetailsParamsList =
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(details)
                    .build()
            val flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(productDetailsParamsList))
                .build()
            _pendingBenefitFlow.value = PurchaseProduct(type, VerifyResult.PENDING)
            client.launchBillingFlow(activity, flowParams)
        } catch (e: Exception) {
            _pendingBenefitFlow.value = PurchaseProduct(type, VerifyResult.FAILED)
            onError()
        }
    }

    private fun receiveProduct(type: BillingProductType?) = scope.launch {
        when (type) {
            BillingProductType.REMOVE_ADS -> {
                userPreferencesDataStoreRepository.setRemoveAds(RemoveAdsStatus.PURCHASED)
            }

            else -> {}
        }
    }

    private fun consumePurchase(purchase: Purchase?) {
        Log.i("TAG", "consumePurchase: $purchase")
        val params =
            ConsumeParams.newBuilder()
                .setPurchaseToken(purchase?.purchaseToken ?: "")
                .build()
        scope.launch {
            val consumeResult = client.consumePurchase(params)
            if (consumeResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                updatePendingBenefitResult(VerifyResult.SUCCESS)
            } else {
                updatePendingBenefitResult(VerifyResult.FAILED)
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase?) {
        Log.i("TAG", "acknowledgePurchase: $purchase")
        if (purchase?.isAcknowledged == true) {
            updatePendingBenefitResult(VerifyResult.SUCCESS)
            return
        }

        val acknowledgeParams =
            AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase?.purchaseToken ?: "")
                .build()
        scope.launch {
            val acknowledgeResult = client.acknowledgePurchase(acknowledgeParams)
            Log.i("TAG", "acknowledgePurchase: $acknowledgeResult")
            if (acknowledgeResult.responseCode == BillingClient.BillingResponseCode.OK) {
                updatePendingBenefitResult(VerifyResult.SUCCESS)
            } else {
                updatePendingBenefitResult(VerifyResult.FAILED)
            }
        }
    }

    private fun updatePendingBenefitResult(result: VerifyResult) {
        _pendingBenefitFlow.value = pendingBenefitFlow.value.copy(result = result)

        if (result == VerifyResult.SUCCESS) {
            receiveProduct(pendingBenefitFlow.value.type)
        }
    }

    private fun updatePendingBenefitType(json: JSONObject?) {
        val type = BillingProductType.entries.find { billingProductType ->
            json?.optString("productId") == billingProductType.id
        }
        _pendingBenefitFlow.value = pendingBenefitFlow.value.copy(type = type)
    }

    fun restorePurchases() = scope.launch {
        val historyParams = QueryPurchaseHistoryParams.newBuilder().setProductType(
            ProductType.INAPP
        ).build()

        val (billingResult, _) = client.queryPurchaseHistory(historyParams)
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            queryPurchases()
        }
    }
}

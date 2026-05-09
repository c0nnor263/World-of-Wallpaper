package com.doodle.turboracing3.presentation

import androidx.lifecycle.ViewModel
import com.doodle.core.domain.enums.RemoveAdsStatus
import com.doodle.core.domain.source.local.repository.UserPreferencesDataStoreRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class AppContentViewModel(
//    private val billingDataSource: BillingDataSource,
//    private val appOpenAdManager: AppOpenAdManager,
//    private val nativeAdManager: NativeAdManager,
    userPreferencesDataStoreRepository: UserPreferencesDataStoreRepository
) : ViewModel() {
    val isPremiumUser: Flow<RemoveAdsStatus> =
        userPreferencesDataStoreRepository.getRemoveAdsStatusFlow()

    init {
//        billingDataSource.initClient()
    }

    override fun onCleared() {
        super.onCleared()
//        billingDataSource.endConnection()
    }

    fun onResumeBilling() {
//        billingDataSource.onResumeBilling()
    }

    fun showAppOpenAd() {
//        appOpenAdManager.showAdIfAvailable(activity)
    }

    fun destroyNativeAds() {
//        nativeAdManager.onActivityDestroy()
    }
}

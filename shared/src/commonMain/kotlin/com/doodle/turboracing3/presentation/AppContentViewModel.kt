package com.doodle.turboracing3.presentation

import androidx.lifecycle.ViewModel
import com.doodle.core.domain.enums.RemoveAdsStatus
import com.doodle.core.domain.source.local.repository.AppPreferencesDataStoreRepository
import com.doodle.core.domain.source.local.repository.UserPreferencesDataStoreRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class AppContentViewModel(
//    private val billingDataSource: BillingDataSource,
//    private val nativeAdManager: NativeAdManager,
    userPreferencesDataStoreRepository: UserPreferencesDataStoreRepository,
    appPreferencesDataStoreRepository: AppPreferencesDataStoreRepository
) : ViewModel() {
    val isPremiumUser: Flow<RemoveAdsStatus> =
        userPreferencesDataStoreRepository.getRemoveAdsStatusFlow()
    val isAvailableForAppOpenAd: Flow<Boolean> =
        appPreferencesDataStoreRepository.getIsAvailableForAppOpenAdFlow()


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

    fun destroyNativeAds() {
//        nativeAdManager.onActivityDestroy()
    }
}

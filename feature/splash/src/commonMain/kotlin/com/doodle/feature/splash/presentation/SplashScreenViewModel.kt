package com.doodle.feature.splash.presentation

import androidx.lifecycle.ViewModel
import com.doodle.core.domain.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SplashScreenViewModel(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
//    private val appPreferencesDataStoreRepository: AppPreferencesDataStoreRepository,
//    private val appOpenAdManager: AppOpenAdManager
) : ViewModel() {
//    val appOpenAdStatus = appOpenAdManager.adStatus

//    fun showAppOpenAd(activity: ComponentActivity) {
//        appOpenAdManager.showAdIfAvailable(activity)
//    }

//    fun incrementAppOpenTimes() = viewModelScope.launch(ioDispatcher) {
//        appPreferencesDataStoreRepository.incrementAppOpenTimes()
//    }
}

package com.doodle.feature.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doodle.core.domain.di.IoDispatcher
import com.doodle.core.domain.source.local.repository.AppPreferencesDataStoreRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SplashScreenViewModel(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val appPreferencesDataStoreRepository: AppPreferencesDataStoreRepository,
) : ViewModel() {
    val isAvailableForAppOpenAd: Flow<Boolean> =
        appPreferencesDataStoreRepository.getIsAvailableForAppOpenAdFlow()

    fun incrementAppOpenTimes() = viewModelScope.launch(ioDispatcher) {
        appPreferencesDataStoreRepository.incrementAppOpenTimes()
    }
}

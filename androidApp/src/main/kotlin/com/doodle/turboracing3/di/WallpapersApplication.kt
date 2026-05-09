package com.doodle.turboracing3.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class WallpapersApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@WallpapersApplication)
            androidLogger()
        }
    }
}

package com.doodle.turboracing3.di

import org.koin.core.KoinApplication
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes
import org.koin.plugin.module.dsl.startKoin

fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    return startKoin<CommonWallpapersApplication> {
        printLogger()
        includes(config)
    }
}


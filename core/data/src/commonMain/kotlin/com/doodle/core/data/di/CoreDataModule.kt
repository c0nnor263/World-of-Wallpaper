package com.doodle.core.data.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module

@Module(
    includes = [
        PreferencesModule::class,
        PreferencesStorageModule::class
    ]
)
@Configuration
@ComponentScan("com.doodle.core.data")
class CoreDataModule
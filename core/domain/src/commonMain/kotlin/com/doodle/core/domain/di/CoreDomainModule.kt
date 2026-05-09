package com.doodle.core.domain.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module

@Module(includes = [
    CoroutineModule::class,
])
@Configuration
@ComponentScan("com.doodle.core.domain")
class CoreDomainModule
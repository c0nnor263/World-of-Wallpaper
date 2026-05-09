package com.doodle.core.database.di

import com.doodle.core.database.FavoriteDatabaseBuilder
import org.koin.core.annotation.Factory
import org.koin.core.scope.Scope

expect class FavoriteDatabaseModule {
    @Factory
    fun provideBuilder(scope: Scope): FavoriteDatabaseBuilder
}
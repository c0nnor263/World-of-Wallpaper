package com.doodle.core.picturedetails.di

import com.doodle.core.data.di.RemoteSourceModule
import com.doodle.core.domain.source.remote.RemoteImagePixabaySource
import com.doodle.core.domain.source.remote.repository.RemoteImagePagingRepository
import com.doodle.core.picturedetails.repository.FakeRemoteImagePagingRepositoryImpl
import com.doodle.core.picturedetails.repository.FakeRemoteImagePixabaySourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RemoteSourceModule::class]
)
interface RemoteRepositoryModule {
    @Binds
    fun provideImagesRepository(imagesRepositoryImpl: FakeRemoteImagePagingRepositoryImpl): RemoteImagePagingRepository

    @Binds
    fun provideImageSourceRepository(fetchedImageSourceImpl: FakeRemoteImagePixabaySourceImpl): RemoteImagePixabaySource
}

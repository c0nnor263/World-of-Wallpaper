package com.doodle.core.network.di


import com.doodle.core.network.BuildKonfig
import com.doodle.core.network.source.PixabayService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
@ComponentScan("com.doodle.core.network")
class CoreNetworkModule {
    @Single
    fun providesHttpClient(): HttpClient {
        return HttpClient {
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }
            install(ContentNegotiation) {
                json(Json {
                    encodeDefaults = true
                    isLenient = true
                    coerceInputValues = true
                    ignoreUnknownKeys = true
                })
            }
            defaultRequest {
                url("https://pixabay.com/")
            }
        }
    }

    @Single

    fun providesPixabayService(
        client: HttpClient
    ): PixabayService {
        return PixabayService(
            client = client,
            apiKey = BuildKonfig.PIXABAY_API_KEY
        )
    }
}

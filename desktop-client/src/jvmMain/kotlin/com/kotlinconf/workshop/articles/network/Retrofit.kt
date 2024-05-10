package com.kotlinconf.workshop.articles.network

import com.kotlinconf.workshop.WorkshopServerConfig
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object WorkshopRetrofitClient {
    @OptIn(ExperimentalSerializationApi::class)
    private val client = run {
        val httpClient = OkHttpClient.Builder().build()
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        val retrofit = Retrofit.Builder()
            .baseUrl(WorkshopServerConfig.WORKSHOP_SERVER_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(httpClient)
            .build()
        retrofit
    }

    fun <T> create(service: Class<T>) = client.create(service)
}
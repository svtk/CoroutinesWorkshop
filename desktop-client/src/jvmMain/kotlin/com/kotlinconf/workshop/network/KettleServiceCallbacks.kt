package com.kotlinconf.workshop.network

import com.kotlinconf.workshop.kettle.CelsiusTemperature
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET

interface KettleServiceCallbacks {
    @GET("kettle/temperature")
    fun getTemperature(): Call<CelsiusTemperature>
}

fun KettleServiceCallbacks.getTemperature(callback: () -> Unit) {
    getTemperature().enqueue(object : Callback<CelsiusTemperature> {
        override fun onResponse(call: Call<CelsiusTemperature>, response: Response<CelsiusTemperature>) {
            TODO("Not yet implemented")
        }

        override fun onFailure(call: Call<CelsiusTemperature>, t: Throwable) {
            TODO("Not yet implemented")
        }
    })
}

fun createKettleServiceCallbacks(): KettleServiceCallbacks {
    return WorkshopRetrofitClient.create(KettleServiceCallbacks::class.java)
}
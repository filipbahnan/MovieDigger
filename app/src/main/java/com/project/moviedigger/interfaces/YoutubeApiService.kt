package com.project.moviedigger.interfaces

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiService {
    @GET("request_trailers")
    fun fetchNewTrailers(): Call<ResponseBody>

    companion object {

        private const val BASE_URL = "http://18.222.177.63:5000/"

        fun createService(): YoutubeApiService {
            val interceptor: HttpLoggingInterceptor =  HttpLoggingInterceptor()
            interceptor.level = (HttpLoggingInterceptor.Level.BODY)
            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(YoutubeApiService::class.java)
        }
    }
}

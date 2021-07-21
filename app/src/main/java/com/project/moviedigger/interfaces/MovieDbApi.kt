package com.project.moviedigger.interfaces

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface MovieDbApi {

    @GET("request_upcoming")
    fun listOfUpcomingNonRegion(): Call<ResponseBody>

    @GET("request_released")
    fun listOfReleasedNonRegion(): Call<ResponseBody>

    @GET("request_released")
    fun listOfReleasedSorted(@Query("genre_ids") genre_ids : String, @Query("vote_average") vote_average : String): Call<ResponseBody>

   companion object {
       private const val BASE_URL = "http://18.222.177.63:5000/"

        fun createService(): MovieDbApi {
            val interceptor =  HttpLoggingInterceptor()
            interceptor.level = (HttpLoggingInterceptor.Level.BODY)
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build().create(MovieDbApi::class.java)
        }
    }
}
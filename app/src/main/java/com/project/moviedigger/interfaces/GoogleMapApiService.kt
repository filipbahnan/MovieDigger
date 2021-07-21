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

interface GoogleMapApiService {

    // Retrofit2
    @GET("maps/api/place/nearbysearch/json")
    fun getNearbyMovieTheaters(
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("types") types: String,
        @Query("sensor") sensor: String,
        @Query("key") key: String
    ): Call<ResponseBody>

    companion object {

        private const val BASE_URL = "https://maps.googleapis.com/"

        fun createService(): GoogleMapApiService {
            val interceptor: HttpLoggingInterceptor =  HttpLoggingInterceptor()
            interceptor.level = (HttpLoggingInterceptor.Level.BODY)
            val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(GoogleMapApiService::class.java)
        }
    }
}
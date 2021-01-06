package com.amatai.lybra_app.requestmanager

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


val  BASE_ROOT = "https://testapp.udenar.edu.co/"

val interceptor:HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    this.level = HttpLoggingInterceptor.Level.BODY
}

val cliente:OkHttpClient = OkHttpClient.Builder().apply {
    this.addInterceptor(interceptor)
}.build()

private val retrofit =  Retrofit.Builder()
    .baseUrl(BASE_ROOT)
    .client(cliente)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

object RetrofitService{
    val retrofitService:ApiService by lazy { retrofit.create(ApiService::class.java) }
}
package com.fatmamuge.halyonetim

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.90.187.184:8080/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
    val loginApi: LoginApi = retrofit.create(LoginApi::class.java)
    val dashboardApi: DashboardApi = retrofit.create(DashboardApi::class.java)
    val userApi: UserApi = retrofit.create(UserApi::class.java)
}
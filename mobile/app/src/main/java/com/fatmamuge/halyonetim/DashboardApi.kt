package com.fatmamuge.halyonetim

import retrofit2.http.GET
import retrofit2.http.Header

interface DashboardApi {

    @GET("dashboard/stats")
    suspend fun getStats(
        @Header("Authorization") token: String
    ): DashboardStats
}
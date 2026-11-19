package com.fatmamuge.halyonetim

import retrofit2.http.GET
import retrofit2.http.Header

interface UserApi {

    @GET("users")
    suspend fun getUsers(
        @Header("Authorization") token: String
    ): List<User>
}
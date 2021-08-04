package com.comvarun.example.network

import com.comvarun.example.model.UserList
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {

    @GET("users")
    suspend fun getUsers(): Response<UserList>

}
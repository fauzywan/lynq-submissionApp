package com.example.lynq.data.remote.retrofit

import com.example.lynq.data.remote.retrofit.body.LoginBody
import com.example.lynq.data.remote.response.RegisterResponse
import com.example.lynq.data.remote.retrofit.body.RegisterBody
import com.example.lynq.data.remote.response.LoginResponse
import com.example.lynq.data.remote.response.StoriesResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("register")
    suspend fun register(
        @Body registerBody: RegisterBody
    ): RegisterResponse

    @POST("login")
    suspend fun authenticate(
        @Body loginBody: LoginBody
    ): LoginResponse
    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = 0
    ): Call<StoriesResponse>

}

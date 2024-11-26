package com.example.lynq.data.remote.retrofit


import com.example.lynq.data.remote.response.RegisterResponse
import com.example.lynq.data.remote.response.LoginResponse
import com.example.lynq.data.remote.response.RequestResponse
import com.example.lynq.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
    @Field("name") name: String,
    @Field("email") email: String,
    @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun authenticate(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

        @Multipart
        @POST("stories")
        suspend fun postStory(
            @Part file: MultipartBody.Part,
            @Header("Authorization") token: String,
            @Part("description") description: RequestBody,
        ): RequestResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
    ): StoryResponse

}

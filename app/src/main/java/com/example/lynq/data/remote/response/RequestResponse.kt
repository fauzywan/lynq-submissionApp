package com.example.lynq.data.remote.response

import com.google.gson.annotations.SerializedName

data class RequestResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
)
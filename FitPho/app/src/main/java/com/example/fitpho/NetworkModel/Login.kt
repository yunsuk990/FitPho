package com.example.fitpho.NetworkModel

import com.google.gson.annotations.SerializedName

//로그인 요청
data class Login(
    @SerializedName(value = "email") var email: String,
    @SerializedName(value = "password") var password: String
)
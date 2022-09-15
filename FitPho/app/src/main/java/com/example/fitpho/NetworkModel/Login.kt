package com.example.fitpho.NetworkModel

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName(value = "id") var id: String,
    @SerializedName(value = "pw ") var pw: String
)
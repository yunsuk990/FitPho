package com.example.fitpho.NetworkModel

import com.google.gson.annotations.SerializedName

data class Register(
    @SerializedName(value = "email") var email: String,
    @SerializedName(value = "password") var password: String,
    @SerializedName(value = "confirm_password") var confirm_password: String,
)

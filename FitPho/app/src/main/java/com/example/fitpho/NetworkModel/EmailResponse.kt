package com.example.fitpho.NetworkModel

import com.google.gson.annotations.SerializedName

class EmailResponse (

    @SerializedName(value = "success") val success: String,
    @SerializedName(value = "message") val message: String
    ){

    public fun printSuccess(): String {
        return success
    }
    public fun printMessage(): String {
        return message
    }
}
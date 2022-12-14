package com.example.fitpho.NetworkModel

import com.google.gson.annotations.SerializedName

//회원탈퇴 (응답)
class WithdrawResponse(
    private val success: String,
    private val message: String
){
     fun getSuccess():String {
        return success
    }

    fun getMessage(): String {
        return message
    }
}

data class Passwd(
    @SerializedName(value = "password") var password: String
)

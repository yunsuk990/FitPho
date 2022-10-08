package com.example.fitpho.NetworkModel


class WithdrawResponse(
    private val success: String,
    private val message: String
){
    public fun getSuccess():String {
        return success
    }

    fun getMessage(): String {
        return message
    }

}

package com.example.fitpho.NetworkModel

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

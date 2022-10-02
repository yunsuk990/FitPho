package com.example.fitpho.NetworkModel

class CorrectionResponse(
    private val success: String,
    private val message: String,
    private val token: String
){
    public fun getToken():String {
        return token
    }
    public fun getSuccess(): String {
        return success
    }
    public fun getMessage(): String {
        return message
    }

}
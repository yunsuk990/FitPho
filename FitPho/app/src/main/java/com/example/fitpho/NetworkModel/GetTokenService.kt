package com.example.fitpho.NetworkModel

//토큰 재발급
data class GetTokenService(
    private val success: String,
    private val message: String,
    private val token: String
){
    public fun getSuccess(): String {
        return success
    }
    public fun getMessage(): String {
        return message
    }
    public fun getToken(): String {
        return token
    }
}

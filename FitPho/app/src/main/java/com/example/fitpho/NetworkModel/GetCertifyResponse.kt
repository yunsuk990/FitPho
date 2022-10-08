package com.example.fitpho.NetworkModel

class GetCertifyResponse(
    private val success: String,
    private val message: String,
    private val authNumber: String,
) {
    fun getSuccess(): String {
        return success
    }
    fun getMessage(): String {
        return message
    }
    fun getauthNumber(): String {
        return authNumber
    }
}
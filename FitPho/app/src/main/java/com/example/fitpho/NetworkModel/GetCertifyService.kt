package com.example.fitpho.NetworkModel

//비밀번호 재설정 전 인증번호 발급
class GetCertifyService(
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
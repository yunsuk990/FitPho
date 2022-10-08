package com.example.fitpho.NetworkModel

//이메일 중복 확인 요청
class EmailResponse (
    private val success: String,
    private val message: String,
    private val authNumber: String
    ){

    public fun printMessage(): String {
        return message
    }
    public fun printSuccess(): String{
        return success
    }
    public fun printAuthNumber(): String{
        return authNumber
    }
}
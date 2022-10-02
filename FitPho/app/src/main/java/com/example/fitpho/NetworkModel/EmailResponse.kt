package com.example.fitpho.NetworkModel

//이메일 중복 확인 요청
class EmailResponse (
    private val success: String,
    private val message: String
    ){

    public fun printMessage(): String {
        return message
    }
    public fun printSuccess(): String{
        return success
    }
}
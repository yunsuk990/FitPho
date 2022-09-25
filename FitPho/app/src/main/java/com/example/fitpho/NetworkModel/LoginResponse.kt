package com.example.fitpho.NetworkModel

class LoginResponse(
    val success: String,
    val message: String,
    val token: String
){
    public fun printMessage(): String {
        return token
    }
}


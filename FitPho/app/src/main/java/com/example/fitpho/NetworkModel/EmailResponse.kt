package com.example.fitpho.NetworkModel

class EmailResponse (
    val success: String,
    val message: String
    ){
    public fun printMessage(): String {
        return message
    }

    public fun printSuccess(): String{
        return success
    }
}
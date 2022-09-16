package com.example.fitpho.NetworkModel

class LoginResponse(val isSucess: String){
    public fun printMessage(): String {
        return isSucess
    }
}


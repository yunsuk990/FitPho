package com.example.fitpho.Network

import com.example.fitpho.NetworkModel.Login
import com.example.fitpho.NetworkModel.LoginResponse
import com.example.fitpho.NetworkModel.Register
import com.example.fitpho.NetworkModel.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface API {

    @POST("/login")
    fun signIn(@Body login:Login): Call<LoginResponse>
    //AuthResponse -> Body로 들어오는 데이터

    @POST("/register")
    fun registerIn(@Body register: Register): Call<RegisterResponse>
}
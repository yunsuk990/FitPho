package com.example.fitpho.Network

import com.example.fitpho.NetworkModel.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface API {

    @POST("/auth/login")
    fun signIn(@Body login:Login): Call<LoginResponse>
    //AuthResponse -> Body로 들어오는 데이터

    @POST("/auth/register")
    fun registerIn(@Body register: Register): Call<RegisterResponse>

    @GET("/auth/email") //Body가 아닌 Query로 줘야함
    fun emailConfirm(
        @Query("email") email:String
    ): Call<EmailResponse>
}
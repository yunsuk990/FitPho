package com.example.fitpho.Network

import android.provider.Contacts.SettingsColumns.KEY
import com.example.fitpho.NetworkModel.*
import retrofit2.Call
import retrofit2.http.*

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


    //로그아웃 - 구현해야됨
    @GET("/auth/logout")
    fun logOut(
       @Header("Authorization") token: String
    ): Call<LogOutResponse>

    @POST("/auth/token")
    fun getToken(): Call<GetTokenResponse>

    @DELETE("/auth/delete")
    fun withdraw(
        @Header("Authorization") token: String
    ): Call<WithdrawResponse>

    @POST("/auth/edit")
    fun correction(
        @Header("Authorization") token: String
    ): Call<CorrectionResponse>
}


package com.example.fitpho.Network

import android.os.Build.VERSION_CODES.S
import android.provider.Contacts.SettingsColumns.KEY
import com.example.fitpho.NetworkModel.*
import retrofit2.Call
import retrofit2.http.*

interface API {

    //로그인
    @POST("/auth/login")
    fun signIn(@Body login:Login): Call<LoginResponse>
    //AuthResponse -> Body로 들어오는 데이터

    //회원가입
    @POST("/auth/register")
    fun registerIn(@Body register: Register): Call<RegisterResponse>

    //Email 중복 확인
    @GET("/auth/email/{email}") //Body가 아닌 Query로 줘야함
    fun emailConfirm(
        @Path("email") email: String
    ): Call<EmailResponse>

    //로그아웃 - 구현해야됨
    @GET("/auth/logout")
    fun logOut(
       @Header("Authorization") token: String
    ): Call<LogOutResponse>

    //Token 가져오기
    @POST("/auth/token")
    fun getToken(): Call<GetTokenResponse>

    //회원탈퇴
    @DELETE("/auth/delete")
    fun withdraw(
        @Header("Authorization") token: String
    ): Call<WithdrawResponse>

    //비밀번호 변경
    @PATCH("/auth/edit")
    fun correction(
        @Body correction: Correction,
        @Header("Authorization") token: String
    ): Call<CorrectionResponse>

    //가이드 전체 조회
    @GET("/auth/library")
    fun guideAllData(): Call<GuideDataResponse>

    //가이드 부위 조회
    @GET("/library/{part}")
    fun guideData(
        @Path("part") part: String
    ): Call<GuideDataResponse>

    //각 운동 기구 별 세부 내용 조회
    @GET("/library/detail/{id}")
    fun guideDetailData(
        @Path("id") id: Int
    ): Call<GuideDetailResponse>

    //토큰 재발급
    @POST("/auth/token")
    fun getReToken(): Call<GetTokenResponse>


    //비밀번호 재설정
    @PATCH("/auth/resetPW")
    fun findPasswd(
        @Body resetPW: FindPasswd
    ): Call<FindPasswdResponse>

    //비밀번호 전 인증번호 발급
    @GET("/auth/certify/{email}")
    fun certifyPasswd(
        @Path("email") email: String
    ): Call<GetCertifyResponse>

    //즐겨찾기 조회
    @GET("/favorites")
    fun getFavorites(
        @Header("Authorization") token: String
    ): Call<GetFavoritesResponse>

    //즐겨찾기 추가
    @POST("/favorites/{id}")
    fun addFavorites(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<GetAddFavoritesResponse>

    //즐겨찾기 삭제
    @DELETE("/favorites/{id}")
    fun deleteFavorites(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<GetDeleteFavoritesResponse>

}


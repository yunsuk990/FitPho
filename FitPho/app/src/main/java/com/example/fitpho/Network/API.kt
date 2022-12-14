package com.example.fitpho.Network

import com.example.fitpho.NetworkModel.*
import retrofit2.Call
import retrofit2.http.*
import java.util.*

//API
interface API {
    //로그인
    @POST("/auth/login")
    fun signIn(@Body login:Login): Call<LoginResponse>

    //회원가입
    @POST("/auth/register")
    fun registerIn(@Body register: Register): Call<RegisterResponse>

    //Email 중복 확인
    @GET("/auth/email/{email}")
    fun emailConfirm(
        @Path("email") email: String
    ): Call<EmailService>

    //로그아웃
    @GET("/auth/logout")
    fun logOut(
       @Header("Authorization") token: String
    ): Call<LogOutService>

    //Token 가져오기
    @POST("/auth/token")
    fun getToken(): Call<GetTokenService>

    //회원탈퇴
    @POST("/auth/delete")
    fun withdraw(
        @Header("Authorization") token: String,
        @Body passwd: Passwd
    ): Call<WithdrawResponse>

    //가이드 전체 조회
    @GET("/auth/library")
    fun guideAllData(): Call<GuideDataResponse>

    //가이드 부위 조회
    @GET("/library/{part}")
    fun guideData(
        @Path("part") part: String,
        @Header("Authorization") token: String
    ): Call<GuideDataResponse>?

    //각 운동 기구 별 세부 내용 조회
    @GET("/library/detail/{id}")
    fun guideDetailData(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<GuideDetailResponse>

    //토큰 재발급
    @POST("/auth/token")
    fun getReToken(): Call<GetTokenService>


    //비밀번호 재설정
    @PATCH("/auth/resetPW")
    fun findPasswd(
        @Body resetPW: FindPasswd
    ): Call<FindPasswdResponse>

    //비밀번호 전 인증번호 발급
    @GET("/auth/certify/{email}")
    fun certifyPasswd(
        @Path("email") email: String
    ): Call<GetCertifyService>

    //즐겨찾기 조회
    @GET("/favorites")
    fun getFavorites(
        @Header("Authorization") token: String
    ): Call<GetFavoritesResponse>?

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

    //캘린더 전체 일정 조회
    @GET("/calendar")
    fun ScheduleGetDot(
        @Header("Authorization") token: String
    ): Call<ScheduleGetDot>

    //캘린더 일정 추가
    @POST("/calendar/{date}/{tvStart}")
    fun ScheduleSave(
        @Path("date") date: String,
        @Path("tvStart") tvStart: String,
        @Header("Authorization") token: String,
        @Body calendar: CalendarSave
    ): Call<CalendarSaveResponse>

    //캘린더 일자별 일정 조회
    @GET("/calendar/{date}")
    fun ScheduleAdd(
        @Path("date") date: String,
        @Header("Authorization") token: String
    ): Call<CalendarRequestResponse>

    //캘린더 세부 일정 조회
    @GET("/calendar/detail/{date}/{tvStart}")
    fun DetailSchedule(
        @Path("date") date: String,
        @Path("tvStart") tvStart: String,
        @Header("Authorization") token: String
    ): Call<CalendarDetailResponse>

    //캘린더 일정삭제
    @DELETE("/calendar/{date}/{tvStart}")
    fun ScheduleDelete(
        @Path("date") date: String,
        @Path("tvStart") tvStart: String,
        @Header("Authorization") token: String
    ): Call<CalendarDeleteResponse>

    //캘린더 일정수정
    @PATCH("/calendar/{date}/{tvStart}")
    fun ScheduleUpdate(
        @Path("date") date: String,
        @Path("tvStart") tvStart: String,
        @Header("Authorization") token: String,
        @Body scheduleUpdate: ScheduleUpdate
    ): Call<CalendarUpdateResponse>

    //Ai 동작인식 운동리스트 받아오기
    @GET("/library/motion")
    fun AiMoveItemList(
        @Header("Authorization") token: String
    ): Call<AiMoveResponse>
}


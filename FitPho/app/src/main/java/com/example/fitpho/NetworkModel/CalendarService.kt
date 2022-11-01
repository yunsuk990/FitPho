package com.example.fitpho.NetworkModel

import com.google.gson.annotations.SerializedName

//캘린더 저장요청 응답
class CalendarSaveResponse(
    private var success: String,
    private var message: String
){
    fun getSuccess(): String{
        return success
    }
    fun getMessage(): String{
        return message
    }
}

//캘린더 저장요청 요청
data class CalendarSave(
    @SerializedName("tvTitle") val tvTitle: String,
    @SerializedName("tvDate") val tvDate: String,
    @SerializedName("tvStart") val tvStart: String,
    @SerializedName("tvEnd") val tvEnd: String,
    @SerializedName("tvContent") val tvContent: String
)


//캘린더 계힉요청 응답
data class CalendarRequestResponse(
    private var success: String,
    private var message: String,
    private var data: ArrayList<schedule>
){
    fun getSuccess(): String{
        return success
    }
    fun getMessage(): String{
        return message
    }
    fun getData(): ArrayList<schedule>{
        return data
    }
}

data class schedule(
    val tvTitle: String,
    val tvDate: String,
    val tvStart: String,
    val tvEnd: String,
    val tvContent: String
)

//캘린더 삭제요청 응답
class CalendarDeleteResponse(
    private var success: String,
    private var message: String
){
    fun getSuccess(): String{
        return success
    }
    fun getMessage(): String{
        return message
    }
}

//캘린더 계획수정 응답
class CalendarUpdateResponse(
    private var success: String,
    private var message: String
){
    fun getSuccess(): String{
        return success
    }
    fun getMessage(): String{
        return message
    }
}

//캘린더 계획수정 요청
data class ScheduleUpdate(
    @SerializedName("tvTitle") val tvTitle: String,
    @SerializedName("tvDate") val tvDate: String,
    @SerializedName("tvStart") val tvStart: String,
    @SerializedName("tvEnd") val tvEnd: String,
    @SerializedName("tvContent") val tvContent: String
)



class CalendarDetailResponse(
    private var success: String,
    private var message: String,
    private var data: ArrayList<schedule>
){
    fun getSuccess(): String{
        return success
    }
    fun getMessage(): String{
        return message
    }
    fun getData(): ArrayList<schedule>{
        return data
    }
}

//캘린더 전체 일정 조회
class ScheduleGetDot(
    private var success: String?,
    private var message: String?,
    private var data: ArrayList<String>?
){
    fun getSuccess(): String?{
        return success
    }
    fun getMessage(): String?{
        return message
    }
    fun getData(): ArrayList<String>?{
        return data
    }
}
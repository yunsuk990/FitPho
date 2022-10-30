package com.example.fitpho.NetworkModel

import com.google.gson.annotations.SerializedName

// 회원정보 수정 - 비밀번호 변경 (요청)
data class Correction(
    @SerializedName(value = "old_password") var old_password: String,
    @SerializedName(value = "new_password") var new_password: String,
)

// 회원정보 수정 - 비밀번호 변경 (응답)
class CorrectionResponse(
    private val success: String,
    private val message: String,
    private val token: String
){
    public fun getToken():String {
        return token
    }
    public fun getSuccess(): String {
        return success
    }
    public fun getMessage(): String {
        return message
    }
}

data class Passwd(
    @SerializedName(value = "old_password") var old_password: String
)



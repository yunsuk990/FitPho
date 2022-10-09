package com.example.fitpho.NetworkModel

import com.google.gson.annotations.SerializedName

// 비밀번호 재설정 (응답)
data class FindPasswdResponse(
    var success: String,
    var message: String
)

// 비밀번호 재설정 (요청)
data class FindPasswd(
    @SerializedName("email") var email: String,
    @SerializedName("new_password") var new_password: String
)

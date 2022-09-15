package com.example.fitpho.NetworkModel

import com.google.gson.annotations.SerializedName

data class Register(
    @SerializedName(value = "id") var id: String,
    @SerializedName(value = "pw") var pw: String,
    @SerializedName(value = "checkpw") var checkpw: String,
    @SerializedName(value = "nickname") var nickname: String
)

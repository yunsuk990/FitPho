package com.example.fitpho.NetworkModel

import com.google.gson.annotations.SerializedName

data class Correction(
    @SerializedName(value = "old_password") var old_password: String,
    @SerializedName(value = "new_password") var new_password: String,
)

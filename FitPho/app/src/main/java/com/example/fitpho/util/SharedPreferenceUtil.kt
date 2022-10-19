package com.example.fitpho.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtil(context: Context) {
    private val TokenPref: SharedPreferences = context.getSharedPreferences("token", Activity.MODE_PRIVATE)
    private val AutoLogInPref: SharedPreferences = context.getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)

    fun getAutoLogin():Boolean {
        val pref = AutoLogInPref
        val id = pref.getString("id", null)
        val pw = pref.getString("pw", null)
        return ((id != null) && (pw != null))
    }

    fun setAutoLogin(id: String?, pw: String?){
        val pref = AutoLogInPref
        val edit = pref.edit()
        edit.putString("id", id)
        edit.putString("pw", pw)
        edit.apply()
    }

    fun deleteAutoLogin(){
        val pref = AutoLogInPref
        val edit = pref.edit()
        edit.clear()
        edit.apply()
    }

    fun getToken(): String?{
        val pref = TokenPref
        return ("token=" + pref.getString("token", null))
    }

    fun setToken(token: String?){
        val pref = TokenPref
        val edit = pref.edit()
        edit.putString("token", token)
        edit.apply()
    }

    fun deleteToken(){
        val pref = TokenPref
        val edit = pref.edit()
        edit.apply()
    }
}
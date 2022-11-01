package com.example.fitpho.Setting

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.*
import com.example.fitpho.R
import com.example.fitpho.databinding.DeleteAccountBinding
import com.example.fitpho.util.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CorrectionDialog(context: Context?) : DialogFragment() {

    private lateinit var binding: DeleteAccountBinding
    private lateinit var prefs: SharedPreferenceUtil

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DeleteAccountBinding.inflate(LayoutInflater.from(context))
        binding.cancel.setOnClickListener{
            dismiss()
        }
        prefs = SharedPreferenceUtil(requireContext())
        binding.verify.setOnClickListener {
            val old_password = binding.oldPassword.text.toString()
            Log.d("비번", old_password.toString())
            verifyPassword(old_password)
        }
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        return builder.create()
    }

    fun verifyPassword(old_password: String) {
        when{
            old_password.isEmpty() -> Toast.makeText(requireContext(), " 기존 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show()
            else -> {
                authService().withdraw(prefs.getToken()!!, Passwd(old_password)).enqueue(object: Callback<WithdrawResponse>{
                    override fun onResponse(
                        call: Call<WithdrawResponse>,
                        response: Response<WithdrawResponse>,
                    ) {
                        when(response.code()){
                            200 -> {
                                prefs.deleteUserEmail()
                                prefs.deleteAutoLogin()
                                prefs.deleteToken()
                                Log.d("Withdraw", "탈퇴성공")
                                Toast.makeText(requireContext(), response.body()?.getMessage(), Toast.LENGTH_LONG).show()
                                dismiss()
                                findNavController().navigate(R.id.action_global_loginFragment)
                            }
                            400 -> {
                                Toast.makeText(requireContext(), response.body()?.getMessage(), Toast.LENGTH_LONG).show()
                                Log.d("Withdraw", "탈퇴실패1")
                            }
                            403 -> {
                                //getReToken()
                                Log.d("Withdraw", "탈퇴실패2")
                            }
                            else -> Log.d("Withdraw", "탈퇴실패3")
                        }
                    }
                    override fun onFailure(call: Call<WithdrawResponse>, t: Throwable) {
                        Log.d("Withdraw", "오류")
                    }
                })
            }
        }
    }

    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }
}

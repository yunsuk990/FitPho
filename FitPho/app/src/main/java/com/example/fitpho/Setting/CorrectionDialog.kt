package com.example.fitpho.Setting

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.Correction
import com.example.fitpho.NetworkModel.CorrectionResponse
import com.example.fitpho.NetworkModel.GetTokenResponse
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.databinding.CorrectionDialogBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CorrectionDialog: DialogFragment() {

    private lateinit var binding: CorrectionDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = CorrectionDialogBinding.inflate(LayoutInflater.from(context))
        binding.cancel.setOnClickListener{
            dismiss()
        }
        binding.verify.setOnClickListener {
            val old_password = binding.oldPassword.text.toString()
            val password = binding.password.text.toString()
            verifyPassword(old_password, password)

        }

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        return builder.create()
    }



    fun verifyPassword(old_password: String, new_password: String) {
        when{
            old_password.isEmpty() -> Toast.makeText(requireContext(), "기존 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show()
            new_password.isEmpty() -> Toast.makeText(requireContext(), "새 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show()
            else -> {
                authService().correction( Correction(old_password, new_password),getToken()).enqueue(object: Callback<CorrectionResponse> {
                    override fun onResponse(
                        call: Call<CorrectionResponse>,
                        response: Response<CorrectionResponse>,
                    ) {
                        when(response.code()){
                            200 -> {
                                Toast.makeText(requireContext(), response.body()?.getMessage(), Toast.LENGTH_LONG).show()
                                dismiss()
                            }
                            400 -> {
                                Toast.makeText(requireContext(), response.body()?.getMessage(), Toast.LENGTH_LONG).show()
                            }
                            403 -> {
                                //getReToken()
                            }
                            else -> {
                                Log.d("비밀번호 변경", "비밀번호 변경 실패")
                            }
                        }
                    }
                    override fun onFailure(call: Call<CorrectionResponse>, t: Throwable) {
                        Log.d("비밀번호 변경", "비밀번호 변경 실패")
                    }
                })
            }
        }
    }

    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }

    private fun getToken(): String{
        val pref = activity?.getSharedPreferences("TOKEN",0)
        val token = "token="+ pref?.getString("token","")!!
        return token
    }

    //토큰 재발급
    private fun getReToken(){
        authService().getReToken().enqueue(object: Callback<GetTokenResponse>{
            override fun onResponse(
                call: Call<GetTokenResponse>,
                response: Response<GetTokenResponse>,
            ) {
                when(response.code()){
                    200 -> {
                        Toast.makeText(requireContext(), response.body()?.getMessage(), Toast.LENGTH_LONG)
                        val pref = requireActivity().getSharedPreferences("TOKEN",0)
                        var editor = pref.edit()
                        editor.clear()
                        editor.putString("token", response.body()?.getToken())
                        editor.apply()
                    }

                    401 -> {
                        Toast.makeText(requireContext(), response.body()?.getMessage(), Toast.LENGTH_LONG)
                    }

                    403 -> {
                        Toast.makeText(requireContext(), response.body()?.getMessage(), Toast.LENGTH_LONG)
                    }

                    else -> {
                        Log.d("getReToken", "getReToken fail")
                    }
                }
            }

            override fun onFailure(call: Call<GetTokenResponse>, t: Throwable) {
                Log.d("getReToken", "getReToken failure")
            }

        })
    }
}

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



    fun verifyPassword(old_password: String, password: String) {
        when{
            old_password.isEmpty() -> Toast.makeText(requireContext(), "기존 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show()
            password.isEmpty() -> Toast.makeText(requireContext(), "새 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show()
            else -> {
                authService().correction( Correction(old_password, password),getToken()).enqueue(object: Callback<CorrectionResponse> {
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
}

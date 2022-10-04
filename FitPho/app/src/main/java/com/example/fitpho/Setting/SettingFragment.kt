package com.example.fitpho.Setting

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.CorrectionResponse
import com.example.fitpho.NetworkModel.LogOutResponse
import com.example.fitpho.NetworkModel.WithdrawResponse
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentSettingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingFragment : Fragment(){

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authService = authService()

        binding.logout.setOnClickListener{
            authService.logOut(getToken()).enqueue(object: Callback<LogOutResponse>{
                override fun onResponse(
                    call: Call<LogOutResponse>,
                    response: Response<LogOutResponse>,
                ) {
                    when(response.code()){
                        200 -> {
                            Toast.makeText(requireContext(), "로그아웃 성공.", Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.action_global_loginFragment)
                        }
                    }
                }

                override fun onFailure(call: Call<LogOutResponse>, t: Throwable, ) {
                    Log.d("LogOutFail", "LogOutFail")
                }
            })
        }

        binding.withdraw.setOnClickListener{
            authService.withdraw(getToken()).enqueue(object: Callback<WithdrawResponse>{
                override fun onResponse(
                    call: Call<WithdrawResponse>,
                    response: Response<WithdrawResponse>,
                ) {
                   when(response.code()){
                       200 -> {
                           Log.d("Withdraw", "탈퇴성공")
                           findNavController().navigate(R.id.action_global_loginFragment)
                       }
                       else -> Log.d("Withdraw", "탈퇴실패")
                   }
                }

                override fun onFailure(call: Call<WithdrawResponse>, t: Throwable) {
                    Log.d("Withdraw", "오류")
                }
            })
        }

        binding.correction.setOnClickListener{
            openDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //API 가져오기
    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }


    //토큰 가져오기
    private fun getToken(): String{
        val pref = activity?.getSharedPreferences("TOKEN",0)
        var token = "token="+ pref?.getString("token","")!!
        return token
    }

    //비밀번호 변경
    private fun openDialog(){
        CorrectionDialog().show(parentFragmentManager, "dialog")
    }
}
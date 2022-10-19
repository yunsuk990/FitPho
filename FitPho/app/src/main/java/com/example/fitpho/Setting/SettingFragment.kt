package com.example.fitpho.Setting

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.video.VideoRecordEvent.Resume
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.*
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentSettingBinding
import com.example.fitpho.util.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingFragment : Fragment(){

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    companion object{
        lateinit var prefs: SharedPreferenceUtil
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        var mytoolbar  = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(mytoolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = SharedPreferenceUtil(requireContext())

        val authService = authService()

        //로그아웃
        binding.btnLogout.setOnClickListener{
            authService.logOut(prefs.getToken()!!).enqueue(object: Callback<LogOutResponse>{
                override fun onResponse(
                    call: Call<LogOutResponse>,
                    response: Response<LogOutResponse>,
                ) {
                    when(response.code()){
                        200 -> {
                            prefs.deleteAutoLogin()
                            prefs.deleteToken()
                            Toast.makeText(requireContext(), "로그아웃 성공.", Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.action_global_loginFragment)
                        }
                        403 -> {
                            Log.d("LogOut", "FAIL")
                        }
                    }
                }

                override fun onFailure(call: Call<LogOutResponse>, t: Throwable, ) {
                    Log.d("LogOutFail", "LogOutFail")
                }
            })
        }

        //회원탈퇴
        binding.btnDelete.setOnClickListener{
            authService.withdraw(prefs.getToken()!!).enqueue(object: Callback<WithdrawResponse>{
                override fun onResponse(
                    call: Call<WithdrawResponse>,
                    response: Response<WithdrawResponse>,
                ) {
                    when(response.code()){
                        200 -> {
                            prefs.deleteAutoLogin()
                            prefs.deleteToken()
                            Log.d("Withdraw", "탈퇴성공")
                            Toast.makeText(requireContext(), response.body()?.getMessage(), Toast.LENGTH_LONG).show()
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

        binding.btnPasswordChange.setOnClickListener{
            openDialog()
        }

        //이용약관
        binding.btnSeeService.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                var intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://leaf-jumpsuit-ad1.notion.site/FitPho-82d1dfcca73748c781af62d5c2f592a0"))
                startActivity(intent)
            }
        })

        //개인정보처리방침
        binding.btnSeePersonal.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                var intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://leaf-jumpsuit-ad1.notion.site/FitPho-924abc7e50e144789728dfcfc79c5aae"))
                startActivity(intent)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //API 가져오기
    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }

    //비밀번호 변경
    private fun openDialog(){
        CorrectionDialog().show(parentFragmentManager, "dialog")
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
                        editor.commit()

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
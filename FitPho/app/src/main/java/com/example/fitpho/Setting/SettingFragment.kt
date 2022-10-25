package com.example.fitpho.Setting

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
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
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.*
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentSettingBinding
import com.example.fitpho.util.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingFragment : Fragment() {

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
                            prefs.deleteUserEmail()
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
            val dialog = CorrectionDialog()
            dialog.show(parentFragmentManager, "dialog")

        }

        //비밀번호 변경
        binding.btnPasswordChange.setOnClickListener{
            findNavController().navigate(R.id.newPasswordFragment, Bundle().apply {
                putString("email", prefs.getUserEmail())
            })
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

        binding.btnSeeLicense.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                var intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://leaf-jumpsuit-ad1.notion.site/FitPho-816f04e30cbe4ce895cd9c92699006e6"))
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
}
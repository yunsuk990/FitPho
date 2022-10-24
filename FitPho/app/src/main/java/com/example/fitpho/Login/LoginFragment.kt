package com.example.fitpho.Login

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.GetTokenResponse
import com.example.fitpho.NetworkModel.Login
import com.example.fitpho.NetworkModel.LoginResponse
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentLoginBinding
import com.example.fitpho.util.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {


    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    companion object{
        lateinit var prefs: SharedPreferenceUtil
    }
    var id: String = ""
    var pw: String = ""
    val authService = authService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = SharedPreferenceUtil(requireContext())

        //회원가입 버튼 클릭
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        //비밀번호 찾기
        binding.findPasswd.setOnClickListener {
            findNavController().navigate(R.id.findPasswordFragment)
        }

        //로그인 버튼 클릭
        binding.btnLogin.setOnClickListener{
            //Test
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

            id = binding.userId.text.toString()
            pw = binding.userPasswd.text.toString()
            if(checkLogin(id, pw)){
                LoginService(id,pw)
            }else{
                Toast.makeText(requireContext(), "아이디/비밀번호 입력하세요.", Toast.LENGTH_LONG)
            }
        }
    }

    //로그인 서비스
    private fun LoginService(id: String, pw: String) {
        authService.signIn(Login(id, pw)).enqueue(object: Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>,
            ) {

                when(response.code()){
                    in (200..299) -> {
                        Log.d("LoginService", "success")
                        binding.progressBar.visibility = View.GONE
                        var token = response.body()?.token
                        if(binding.autoLogin.isChecked){
                            prefs.setAutoLogin(id,pw)
                        }
                        prefs.setToken(token)
                        prefs.setUserEmail(id)
                        Log.d("token", token.toString())
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                    400 -> {
                        Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                    }

                    401 -> {
                        Toast.makeText(requireContext(), "존재하지 않는 이메일 입니다.", Toast.LENGTH_LONG).show()
                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("SIGNIN/FAILURE", t.message.toString())
            }
        })
    }


    //Retrofit api
    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }

    //토큰 재발급
    private fun getReToken(){
        authService().getReToken().enqueue(object: Callback<GetTokenResponse> {
            override fun onResponse(
                call: Call<GetTokenResponse>,
                response: Response<GetTokenResponse>,
            ) {
                when(response.code()){
                    200 -> {
                        var token = "token=" + response.body()?.getToken()
                        Toast.makeText(requireContext(), response.body()?.getMessage(), Toast.LENGTH_LONG)
                        if(prefs.getToken() !=  token){
                            Log.d("getReToken","토큰업데이트")
                            prefs.deleteToken()
                            prefs.setToken(token)
                        }

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


    //로그인 유효성 확인
    private fun checkLogin(id: String, passwd: String): Boolean{
        return !(id.isBlank() || passwd.isBlank())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


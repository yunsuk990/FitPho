package com.example.fitpho.Login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.GetTokenService
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentWelcomeBinding
import com.example.fitpho.util.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    companion object{
        lateinit var prefs: SharedPreferenceUtil
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = SharedPreferenceUtil(requireContext())

        //Test
//      prefs.deleteToken()
//      prefs.deleteAutoLogin()

        binding.emailBtn.setOnClickListener{
            findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
        }

        binding.login.setOnClickListener{
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        //자동 로그인 검사
        if(prefs.getAutoLogin()){
            Log.d("토큰: ", prefs.getToken().toString())
            findNavController().navigate(R.id.homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Retrofit api
    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }

    //토큰 재발급
    private fun getReToken(){
        authService().getReToken().enqueue(object: Callback<GetTokenService> {
            override fun onResponse(
                call: Call<GetTokenService>,
                response: Response<GetTokenService>,
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
            override fun onFailure(call: Call<GetTokenService>, t: Throwable) {
                Log.d("getReToken", "getReToken failure")
            }
        })
    }
}
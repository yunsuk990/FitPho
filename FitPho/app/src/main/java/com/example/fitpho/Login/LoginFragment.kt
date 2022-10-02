package com.example.fitpho.Login

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
import com.example.fitpho.NetworkModel.Login
import com.example.fitpho.NetworkModel.LoginResponse
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {


    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    var id: String = ""
    var pw: String = ""

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

        val authService = authService()

        //회원가입창 이동
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener{
            //Test
            //findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

            id = binding.userId.text.toString()
            pw = binding.userPasswd.text.toString()
            if(checkLogin(id, pw)){
                authService.signIn(getUser()).enqueue(object: Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>,
                    ) {

                        when(response.code()){
                            in (200..299) -> {
                                Log.d("SUCCESS", "success")
                                binding.progressBar.visibility = View.GONE
                                var token = response.body()?.token
                                val pref = requireActivity().getSharedPreferences("TOKEN",0)
                                var editor = pref.edit()
                                editor.putString("token", token)
                                editor.apply()
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
            }else{
                Toast.makeText(requireContext(), "아이디/비밀번호 입력하세요.", Toast.LENGTH_LONG)
            }
        }
    }

    private fun getUser(): Login {
        return Login(id, pw)
    }

    private fun checkLogin(id: String, passwd: String): Boolean{
        return !(id.isBlank() || passwd.isBlank())
    }

    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
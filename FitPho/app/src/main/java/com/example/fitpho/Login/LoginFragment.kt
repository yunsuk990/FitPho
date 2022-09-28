package com.example.fitpho.Login

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


        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener{
            id = binding.userId.text.toString().trim()
            pw = binding.userPasswd.text.toString().trim()
            if(checkLogin(id, pw)){
                val authService = getRetrofit().create(API::class.java)
                authService.signIn(getUser()).enqueue(object: Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>,
                    ) {
                        if(response.isSuccessful){
                            Log.d("SUCCESS", "success")
                            binding.progressBar.visibility = View.GONE
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        }
                        else{
                            Log.d("SIGNIN/FAILURE", "FAIL")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.fitpho

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.Register
import com.example.fitpho.NetworkModel.RegisterResponse
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.databinding.FragmentRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    var userid: String = ""
    var userpasswd: String = ""
    var checkuserpasswd: String = ""
    var nickname: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            userid = binding.registerUserid.text.toString()
            userpasswd = binding.registerPassword.text.toString()
            checkuserpasswd = binding.registerCheckpassword.text.toString()
            nickname = binding.nickname.text.toString()
            if(checkRegister(userid, userpasswd, checkuserpasswd, nickname)){
                val authService = getRetrofit().create(API::class.java)
                authService.registerIn(getRegister()).enqueue(object: Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>,
                    ) {
                        Log.d("REGISTERIN/SUCCESS", response.toString())
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Log.d("REGISTERIN/FAILURE", t.message.toString())
                    }
                })
            }else{
                Toast.makeText(requireContext(), "아이디/비밀번호/닉네임 다시 입력하세요.", Toast.LENGTH_LONG)
            }
        }
    }

    private fun getRegister(): Register{
        return Register(userid, userpasswd, checkuserpasswd, nickname)
    }

    private fun checkRegister(
        userid: String?,
        userpasswd: String?,
        checkuserpasswd: String?,
        nickname: String?
    ): Boolean{
        return userid!!.isBlank() || userpasswd!!.isBlank() || checkuserpasswd!!.isBlank() || nickname!!.isBlank()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.fitpho

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.Register
import com.example.fitpho.NetworkModel.RegisterResponse
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.databinding.FragmentRegisterBinding
import com.example.fitpho.util.hideKeyboard
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
            when {
                userid.isEmpty() -> {
                    Toast.makeText(requireContext(), "아이디를 입력하세요.", Toast.LENGTH_LONG).show()
                }
                userpasswd.isEmpty() -> {
                    Toast.makeText(requireContext(), "비밀번호를 입력하세요.", Toast.LENGTH_LONG).show()
                }
                userpasswd != checkuserpasswd -> {
                    Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.progressBar2.visibility = View.VISIBLE
                    val authService = getRetrofit().create(API::class.java)
                    authService.registerIn(getRegister()).enqueue(object: Callback<RegisterResponse> {
                        override fun onResponse(
                            call: Call<RegisterResponse>,
                            response: Response<RegisterResponse>,
                        ) {
                            if(response.isSuccessful){
                                binding.progressBar2.visibility = View.GONE
                                Log.d("REGISTERIN/SUCCESS", response.toString())
                                hideKeyboard()
                                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                            }else{
                                Log.d("REGISTERIN/FAILURE", "FAIL")
                            }
                        }

                        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                            Log.d("REGISTERIN/FAILURE", t.message.toString())
                            binding.progressBar2.visibility = View.GONE
                        }
                    })
                }
            }
        }
    }

    private fun getRegister(): Register{
        return Register(userid, userpasswd, checkuserpasswd, nickname)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
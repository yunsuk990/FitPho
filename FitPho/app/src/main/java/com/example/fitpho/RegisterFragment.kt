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
import com.example.fitpho.NetworkModel.*
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
    var confirm: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emailConfirm.setOnClickListener{
            var useremail = binding.registerUserid.text.toString()
            val authService = getRetrofit().create(API::class.java)
            authService.emailConfirm(useremail).enqueue(object: Callback<EmailResponse> {
                override fun onResponse(
                    call: Call<EmailResponse>,
                    response: Response<EmailResponse>
                ) {
                    if(response.isSuccessful){
                        var post: EmailResponse? = response.body();
                        Toast.makeText(requireContext(), post?.printMessage(), Toast.LENGTH_LONG).show()
                        Log.d("Comfirm", "success")
                        confirm = true
                    }else{
                        var post: EmailResponse? = response.body();
                        Toast.makeText(requireContext(), post?.printMessage(), Toast.LENGTH_LONG).show()
                        confirm = false
                    }
                }

                override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                    confirm = false
                }
            })
        }

        binding.btnRegister.setOnClickListener {
            if(confirm == true){
                userid = binding.registerUserid.text.toString()
                userpasswd = binding.registerPassword.text.toString()
                checkuserpasswd = binding.registerCheckpassword.text.toString()

                when {
                    userid.isEmpty() -> {
                        Toast.makeText(requireContext(), "이메일을 입력하세요.", Toast.LENGTH_LONG).show()
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
                                    Log.d("REGISTERIN/SUCCESS", "로그인 성공")
                                    hideKeyboard()
                                    confirm = false
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
    }

    private fun getRegister(): Register{
        return Register(userid, userpasswd, checkuserpasswd)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
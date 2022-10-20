package com.example.fitpho.Login

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.GetCertifyResponse
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentEmailAuthorizationBinding
import com.example.fitpho.databinding.FragmentFindPasswordBinding
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern


class FindPasswordFragment : Fragment() {

    private var _binding: FragmentFindPasswordBinding? = null
    private val binding get() = _binding!!
    lateinit var textLayout: TextInputLayout
    var success: Boolean = false

    //이메일 형식 검사 정규식
    val emailValidation =
        "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFindPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textLayout = binding.textInputLayout1

        binding.userId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkEmail()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.findPasswd.setOnClickListener {
            var useremail = binding.userId.text.toString().trim()
            authService().certifyPasswd(useremail).enqueue(object: Callback<GetCertifyResponse>{
                override fun onResponse(
                    call: Call<GetCertifyResponse>,
                    response: Response<GetCertifyResponse>,
                ) {
                    when(response.code()){
                        200 -> {
                            Toast.makeText(requireContext(), response.body()?.getMessage(), Toast.LENGTH_SHORT).show()
                            var authNumber: String = response.body()?.getauthNumber().toString()
                            Log.d("CertifyEmail", "SUCCESS")
                            findNavController().navigate(R.id.action_findPasswordFragment_to_findPasswordAuthFragment,
                                Bundle().apply{
                                    putString("authNumber", authNumber)
                                    putString("email", useremail)
                                })

                        }
                        400 -> {
                            textLayout.error = "가입되어 있지 않은 이메일입니다."
                            Log.d("CertifyEmail", "FAIL1")
                        }
                    }
                }
                override fun onFailure(call: Call<GetCertifyResponse>, t: Throwable) {
                    Log.d("CertifyEmail", "FAILURE")
                }
            })
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

    private fun checkEmail(): Boolean {
        var layout = binding.textInputLayout1
        var userid = binding.userId.text.toString().trim()
        var p = Pattern.matches(emailValidation, userid)

        if (p) {
            binding.userId.setTextColor(Color.BLACK)
            layout.error = null
            return true
        } else {
            binding.userId.setTextColor(Color.RED)
            layout.error = "이메일 형식이 잘못되었습니다."
            return false
        }
    }
}
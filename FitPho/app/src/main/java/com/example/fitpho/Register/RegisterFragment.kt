package com.example.fitpho.Register

import android.app.ProgressDialog.show
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.*
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentRegisterBinding
import com.example.fitpho.util.hideKeyboard
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RegisterFragment : Fragment(){

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    var userid: String = ""
    var userpasswd: String = ""
    var checkuserpasswd: String = ""
    var checkbox1: Boolean = false
    var checkbox2: Boolean = false
    lateinit var textLayout:TextInputLayout
    var authNumber: String? =""
    var authSuccess: Boolean = false
    var code: Boolean = false
    //이메일 형식 검사 정규식
    val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    val authService = authService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        textLayout =  binding.textInputLayout1
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        checkCheckBox()

        //유효성 검사
        binding.registerUserid.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkEmail()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        //이메일 중복 확인
        binding.authBtn.setOnClickListener {
            authSuccess = emailConfirm()
        }

        //회원가입
        binding.btnRegister.setOnClickListener {
                userid = binding.registerUserid.text.toString()
                userpasswd = binding.registerPassword.text.toString()
                checkuserpasswd = binding.registerCheckpassword.text.toString()
                checkbox1 = binding.checkbox1.isChecked
                checkbox2 = binding.checkbox2.isChecked

                //오류 계속 남.. 고치기
                authSuccess = true

                if(authSuccess){
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
                        !checkbox1 -> {
                            Toast.makeText(requireContext(), "서비스 약관 동의해주세요.", Toast.LENGTH_LONG).show()
                        }
                        !checkbox2 -> {
                            Toast.makeText(requireContext(), "개인정보 수집 동의해주세요.", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            binding.progressBar2.visibility = View.VISIBLE
                            authService.registerIn(getRegister()).enqueue(object: Callback<RegisterResponse> {
                                override fun onResponse(
                                    call: Call<RegisterResponse>,
                                    response: Response<RegisterResponse>,
                                ) {
                                    when(response.code()){
                                        //회원가입 성공
                                        200 -> {
                                            binding.progressBar2.visibility = View.GONE
                                            Log.d("REGISTERIN/SUCCESS", "회원가입 성공.")
                                            hideKeyboard()
                                            findNavController().navigate(R.id.startFragment)
                                        }
                                        else -> {
                                            Toast.makeText(requireContext(), "회원가입 실패.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                                    Log.d("REGISTERIN/FAILURE", t.message.toString())
                                    Toast.makeText(requireContext(), "회원가입 실패.", Toast.LENGTH_SHORT).show()
                                    binding.progressBar2.visibility = View.GONE
                                }
                            })
                        }
                    }
                }else{
                    Toast.makeText(requireContext(), "이메일 인증해주세요.", Toast.LENGTH_SHORT).show()
                }


        }
    }


    private fun getRegister(): Register{
        return Register(userid, userpasswd)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //API 호출
    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }

    //이메일 형식 검사
    private fun checkEmail(): Boolean {
        var layout = binding.textInputLayout1
        var userid = binding.registerUserid.text.toString()
        var p = Pattern.matches(emailValidation, userid)

        if(p){
            binding.registerUserid.setTextColor(Color.BLACK)
            layout.error = null
            return true
        }else{
            binding.registerUserid.setTextColor(Color.RED)
            layout.error = "이메일 형식이 잘못되었습니다."
            return false
        }
    }


    //중복 이메일 확인
    private fun emailConfirm():Boolean {
        var useremail = binding.registerUserid.text.toString()
        if (useremail.isEmpty()) {
            textLayout.error = "이메일 입력해주세요"
        } else {
            authService().emailConfirm(useremail).enqueue(object : Callback<EmailResponse> {
                override fun onResponse(
                    call: Call<EmailResponse>,
                    response: Response<EmailResponse>
                ) {
                    var post: EmailResponse? = response.body()
                    authNumber = post?.printAuthNumber()
                    when (response.code()) {
                        in 200..299 -> {
                            Log.d("EmailConfirm", "EmailConfirm/SUCCESS")
                            Log.d("auth", authNumber.toString())
                            //데이터 전달
                            var bundle: Bundle = Bundle()
                            bundle.putString("authNumber", authNumber)
                            var emailauth: EmailAuthorization = EmailAuthorization()
                            emailauth.arguments = bundle
                            emailauth.show(parentFragmentManager, "dialog")
                            code = true
                            Log.d("code1", code.toString())

                        }
                        400 -> {
                            code = false
                            textLayout.error = "이미 가입된 이메일입니다."
                            Log.d("EmailConfirm", "EmailConfirm/FAIL1")
                        }
                        else -> {
                            code = false
                            Log.d("EmailConfirm", "EmailConfirm/FAIL2")
                            textLayout.error = "통신 오류입니다."
                        }
                    }
                }

                //통신 실패 시
                override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                    Log.d("Comfirm", "FAILURE")
                    textLayout.error = "통신 오류입니다."
                    code = false
                }
            })
        }
        Log.d("code", code.toString())
        return code
    }



    //체크박스 전체확인
    fun checkCheckBox(){
        binding.checkbox0.setOnClickListener {
            if(binding.checkbox0.isChecked){
                binding.checkbox1.isChecked = true
                binding.checkbox2.isChecked = true
            }else{
                binding.checkbox1.isChecked = false
                binding.checkbox2.isChecked = false
            }
        }

        binding.checkbox1.setOnClickListener {
            if(binding.checkbox1.isChecked){
                binding.checkbox0.isChecked = binding.checkbox2.isChecked
            }else{
                binding.checkbox0.isChecked = false
            }
        }

        binding.checkbox2.setOnClickListener {
            if(binding.checkbox2.isChecked){
                binding.checkbox0.isChecked = binding.checkbox1.isChecked
            }else{
                binding.checkbox0.isChecked = false
            }
        }
    }
}
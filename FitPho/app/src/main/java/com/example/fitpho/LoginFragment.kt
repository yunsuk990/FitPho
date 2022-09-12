package com.example.fitpho

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fitpho.DataModel.RetrofitBuilder
import com.example.fitpho.DataModel.User
import com.example.fitpho.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Response

class LoginFragment : Fragment() {


    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

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
            val id = binding.userId.text.toString() //사용자 ID
            val passwd = binding.userPasswd.text.toString() //사용자 Passwd
            checkLogin(id, passwd)
            val user = User()
            user.id = id
            user.passwd = passwd
            Login(user)
            //findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    private fun checkLogin(id: String, passwd: String){
        if(id.isBlank() || passwd.isBlank()){
            Toast.makeText(requireContext(), "아이디/패스워드 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun Login(user: User){
        val call = RetrofitBuilder.api.getLoginResponse(user)
        call.enqueue(object: retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful()){
                    Log.d("RESPONSE", response.body().toString())
                }else{
                    Log.d("RESPONSE", "FAILURE")
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("CONNECTION FAILURE: ", t.localizedMessage)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
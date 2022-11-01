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
import com.example.fitpho.NetworkModel.FindPasswd
import com.example.fitpho.NetworkModel.FindPasswdResponse
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentNewPasswordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewPasswordFragment : Fragment() {


    private var _binding: FragmentNewPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentNewPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setPasswd.setOnClickListener {
            var passwd = binding.passwd.text.toString()
            var checkpasswd = binding.checkPasswd.text.toString()
            var email = arguments?.getString("email")

            if(passwd != checkpasswd){
                Toast.makeText(requireContext(), "비밀번호가 다릅니다.", Toast.LENGTH_LONG)
            }else{
                var findpasswd = FindPasswd(email.toString(), passwd)
                authService().findPasswd(findpasswd).enqueue(object: Callback<FindPasswdResponse>{
                    override fun onResponse(
                        call: Call<FindPasswdResponse>,
                        response: Response<FindPasswdResponse>,
                    ) {
                       when(response.code()){
                           200 -> {
                               Log.d("NewPassword", "SUCCESS")
                               Toast.makeText(requireContext(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                               findNavController().navigate(R.id.loginFragment)
                           }
                           else -> {
                               Log.d("NewPassword", "FAIL1")
                               Toast.makeText(requireContext(), "비밀번호 변경에 실패하였습니다.", Toast.LENGTH_LONG)
                           }
                       }
                    }
                    override fun onFailure(call: Call<FindPasswdResponse>, t: Throwable) {
                        Log.d("NewPassword", "FAILURE")
                    }

                })
            }
        }
    }

    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
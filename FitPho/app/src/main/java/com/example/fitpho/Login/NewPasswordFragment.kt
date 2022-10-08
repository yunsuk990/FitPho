package com.example.fitpho.Login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.databinding.FragmentNewPasswordBinding


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
                //authService().findPasswd(email, passwd)
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
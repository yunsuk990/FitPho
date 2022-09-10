package com.example.fitpho

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fitpho.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

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
            val userid: String = binding.registerUserid.text.toString()
            val userpasswd = binding.registerPassword.text.toString()
            val checkuserpasswd = binding.registerCheckpassword.text.toString()
            var user = "안녕하세요"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
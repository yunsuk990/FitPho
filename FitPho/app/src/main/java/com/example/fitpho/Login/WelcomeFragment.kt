package com.example.fitpho.Login

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentWelcomeBinding
import com.example.fitpho.util.SharedPreferenceUtil

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    companion object{
        lateinit var prefs: SharedPreferenceUtil
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = SharedPreferenceUtil(requireContext())


        binding.emailBtn.setOnClickListener{
            findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
        }

        binding.login.setOnClickListener{
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
    }


    override fun onResume() {
        super.onResume()

        //자동 로그인 검사
        if(prefs.getAutoLogin()){
            findNavController().navigate(R.id.homeFragment)
        }else{
            findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
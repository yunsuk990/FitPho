package com.example.fitpho.Register

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentEmailAuthorizationBinding


class EmailAuthorization : DialogFragment() {

    private lateinit var binding: FragmentEmailAuthorizationBinding
    private var authNumber: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentEmailAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var success: Boolean = false

        authNumber = arguments?.getString("authNumber")

        binding.authorizeBtn.setOnClickListener{
            binding.progressbar.visibility = View.VISIBLE
            var inputAuth = ((""+binding.edit4.text) + binding.edit3.text + binding.edit2.text + binding.edit1.text)
            Log.d("inputauth", inputAuth)
            Log.d("authnumber", authNumber.toString())
            if(inputAuth == authNumber.toString()){
                success = true
                Log.d("inputauth", "성공")
                binding.progressbar.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), "인증되었습니다.", Toast.LENGTH_SHORT).show()
                dismiss().apply {
                    Bundle().apply {
                        putBoolean("authSuccess", success)
                    }
                }
            }else{
                binding.progressbar.visibility = View.INVISIBLE
                success = false
                Toast.makeText(requireContext(), "인증번호 오류입니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
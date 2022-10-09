package com.example.fitpho

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.fitpho.databinding.FragmentFindPasswordAuthBinding

class FindPasswordAuthFragment : Fragment() {


    private var _binding: FragmentFindPasswordAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFindPasswordAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.authorizeBtn.setOnClickListener {
            var inputAuth = ((""+binding.edit4.text) + binding.edit3.text + binding.edit2.text + binding.edit1.text)
            var authNumber = arguments?.getString("authNumber")
            var email = arguments?.getString("email")
            Log.d("authNumber", authNumber.toString())
            Log.d("inputAuth", inputAuth.toString())
            if(inputAuth == authNumber.toString()){
                Toast.makeText(requireContext(), "인증되었습니다.", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_findPasswordAuthFragment_to_newPasswordFragment,
                    Bundle().apply{
                        putString("email", email)
                    })
            }else{
                Toast.makeText(requireContext(), "인증번호가 틀렸습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
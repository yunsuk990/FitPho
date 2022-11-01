package com.example.fitpho.Aimovement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentAiMovementResultBinding

class Ai_movement_result : Fragment() {

    private var _binding: FragmentAiMovementResultBinding? = null
    private val binding get() = _binding!!
    var type: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAiMovementResultBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        type = arguments?.getString("type").toString()
        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.aiMovementChoiceFragment)
        }

        if(type.toInt() == 0){
            binding.resultImage.setImageResource(R.drawable.good)
            binding.resultText.text = "참 잘했어요!"
            binding.resultAdvice.text = "동작을 정확하게 잘 하고 있습니다. 이대로 계속 운동을 진행하셔도 됩니다."

        }else if(type.toInt() == 1){
            binding.resultImage.setImageResource(R.drawable.warning)
            binding.resultText.text = "동작이 미숙합니다"
            binding.resultAdvice.text = "아직 동작을 좀 더 정확하게 할 필요가 있습니다."

        }else if(type.toInt() == 2){
            binding.resultImage.setImageResource(R.drawable.bad)
            binding.resultText.text = "정확한 동작이 아닙니다"
            binding.resultAdvice.text = "정확한 동작으로 하고 있지 않습니다. 지속적으로 잘못된 동작을 수행할 시 부상을 초래할 수 있습니다."
        }

        binding.btnAgain.setOnClickListener {
            findNavController().navigate(R.id.aiMovementChoiceFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
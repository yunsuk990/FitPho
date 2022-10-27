package com.example.fitpho.Aimovement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentAiMovementCameraBinding
import com.example.fitpho.databinding.FragmentScheduleUpdateBinding

class AiMovementCameraFragment : Fragment() {

    private var _binding: FragmentAiMovementCameraBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAiMovementCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var id = arguments?.getInt("id")
        var title = arguments?.getString("title")



    }




}
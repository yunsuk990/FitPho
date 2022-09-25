package com.example.fitpho

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.fitpho.databinding.FragmentGuideBinding
import com.example.fitpho.databinding.FragmentSplashBinding


class GuideFragment : Fragment() {


    private var _binding: FragmentGuideBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chest.setOnClickListener{
            val bundle = bundleOf("Title" to "가슴")
            findNavController().navigate(R.id.action_guideFragment_to_guideDetailFragment, bundle)
        }

    }


}
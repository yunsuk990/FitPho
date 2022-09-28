package com.example.fitpho.Guide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitpho.databinding.FragmentGuideDetailBinding

class GuideDetailFragment : Fragment() {

    private var _binding: FragmentGuideDetailBinding? = null
    private val binding get() = _binding!!
    var result: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGuideDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.title.text = arguments?.getString("Title")
    }


}
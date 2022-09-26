package com.example.fitpho

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fitpho.databinding.FragmentCalenderBinding
import com.example.fitpho.databinding.FragmentGuideBinding


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

        binding.chest.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("title", "가슴")
            val guideDetail = GuideDetailFragment()
            guideDetail.arguments = bundle
            findNavController().navigate(R.id.action_guideFragment_to_guideDetailFragment)

        }
    }


}
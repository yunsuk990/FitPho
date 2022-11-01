package com.example.fitpho.Camera

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentAiImageViewBinding

class Ai_ImageViewFragment : Fragment() {

    private var _binding: FragmentAiImageViewBinding? = null
    private val binding get() = _binding!!
    var id: Int? = 0
    var image: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAiImageViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = arguments?.getInt("id", 0)
        image = arguments?.getParcelable("image")

        binding.image.setImageBitmap(image)

        binding.btnBack.setOnClickListener{
            findNavController().navigate(R.id.homeFragment)
        }

        binding.btnGuide.setOnClickListener {
            findNavController().navigate(
                R.id.detailFragment,
                Bundle().apply {
                    putInt("id", id!!)
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
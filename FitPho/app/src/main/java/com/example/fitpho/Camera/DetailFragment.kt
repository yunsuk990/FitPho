package com.example.fitpho.Camera

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.GuideDetailResponse
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentDetailBinding
import com.example.fitpho.databinding.FragmentGuideDetailBinding
import com.example.fitpho.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailFragment : Fragment() {


    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private var id: Int? = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        id = arguments?.getInt("id")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.d("id", id.toString())
        authService().guideDetailData(id!!).enqueue(object: Callback<GuideDetailResponse>{
            override fun onResponse(
                call: Call<GuideDetailResponse>,
                response: Response<GuideDetailResponse>,
            ) {
                when(response.code()){
                    200 -> {
                        var res = response.body()
                        Glide.with(requireContext()).load(res?.getData()!![0].getStimulate1()).into(binding.stimulate1)
                        Glide.with(requireContext()).load(res?.getData()!![0].getStimulate2()).into(binding.stimulate2)
                        binding.text.text = res?.getData()!![0].getText()
                        Glide.with(requireContext()).load(res?.getData()!![0].getAnimation()).into(binding.animation)
                    }
                    else -> {
                        Log.d("Detail", "Fail1")
                    }
                }
            }
            override fun onFailure(call: Call<GuideDetailResponse>, t: Throwable) {
                Log.d("Detail", "FAILURE")
            }
        })
    }

    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
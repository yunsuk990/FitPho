package com.example.fitpho.Guide

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.GuideDetailResponse
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentGuideDetailBinding
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class GuideDetailFragment : Fragment() {

    private var _binding: FragmentGuideDetailBinding? = null
    private val binding get() = _binding!!
    var id: Int? = 0
    var title: String? =""
    var img: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGuideDetailBinding.inflate(inflater, container, false)

        id = arguments?.getInt("id")
        title = arguments?.getString("title")
        img = arguments?.getString("img1")

        //툴바
        var mytoolbar  = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(mytoolbar)
        //뒤로가기 버튼
        //(activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.title.text = title


        Log.d("id", id.toString())
        Log.d("title", title.toString())
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //각 운동기구별 세부내용 조회

        authService().guideDetailData(id!!).enqueue(object :retrofit2.Callback<GuideDetailResponse>{
            override fun onResponse(
                call: Call<GuideDetailResponse>,
                response: Response<GuideDetailResponse>,
            ) {
                when(response.code()){
                    200 -> {
                        var res = response.body()
                        var text = response.body()?.getText()?.size
                        Glide.with(requireContext()).load(img).into(binding.image)
                        Glide.with(requireContext()).load(res?.getData()!![0].getStimulate1()).into(binding.stimulate1)
                        Glide.with(requireContext()).load(res?.getData()!![0].getStimulate2()).into(binding.stimulate2)
                        var s: String =""
                        for(i in 0..((text?.toInt())?.minus(1)!!)){
                            s+=  res.getText()[i]
                            Log.d("text", res.getText()[i])
                        }
                        binding.text.text = s
                        Glide.with(requireContext()).load(res?.getData()!![0].getAnimation()).into(binding.animation)
                    }
                    else -> {
                        Log.d("GuideDetail", "FAIL1")
                    }
                }
            }
            override fun onFailure(call: Call<GuideDetailResponse>, t: Throwable) {
                Log.d("GuideDetail", t.message.toString())
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
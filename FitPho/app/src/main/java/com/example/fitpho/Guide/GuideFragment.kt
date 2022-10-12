package com.example.fitpho.Guide

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.GuideDataResponse
import com.example.fitpho.NetworkModel.data
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentGuideBinding
import retrofit2.Call
import retrofit2.Response


class GuideFragment : Fragment() {

    private var _binding: FragmentGuideBinding? = null
    private val binding get() = _binding!!


    //GuideAdapter 생성
    private val guideAdapter by lazy { GuideAdapter(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGuideBinding.inflate(inflater, container, false)
        binding.exList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        var mytoolbar  = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(mytoolbar)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val viewmodel = ViewModelProvider(this, ViewModel.Factory(requireActivity().application)).get(ViewModel::class.java)

        //구분선
        var dividerItemDecoration: DividerItemDecoration = DividerItemDecoration(view.context, 1)
        dividerItemDecoration.setDrawable(context?.resources!!.getDrawable(R.drawable.recyclerview_divider))
        binding.exList.addItemDecoration(dividerItemDecoration)

        binding.exList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.exList.adapter = guideAdapter



        binding.bookmark.setOnFocusChangeListener { v, hasFocus ->
            
        }

        binding.chest.setOnFocusChangeListener { v, hasFocus ->
            getGuide("chest")
        }
        binding.arm.setOnFocusChangeListener { v, hasFocus ->
            getGuide("arm")
        }
        binding.back.setOnFocusChangeListener { v, hasFocus ->
            getGuide("back")
        }
        binding.leg.setOnFocusChangeListener { v, hasFocus ->
            getGuide("lower")
        }
        binding.shoulder.setOnFocusChangeListener { v, hasFocus ->
            getGuide("shoulder")
        }
        binding.extra.setOnFocusChangeListener { v, hasFocus ->
            getGuide("etc")
        }
    }


    private fun getGuide(part: String){
        authService().guideData(part).enqueue(object: retrofit2.Callback<GuideDataResponse>{
            override fun onResponse(
                call: Call<GuideDataResponse>,
                response: Response<GuideDataResponse>,
            ) {
                when(response.code()){
                    200 -> {
                        var data: List<data> = ArrayList(response.body()?.getData())
//                            for(a in data){
//                                Log.d("Guide", a.getId().toString())
//                            }
                        guideAdapter.setAllData(data)

                    }
                    else -> Log.d("Guide", "GuideFail")
                }
            }
            override fun onFailure(call: Call<GuideDataResponse>, t: Throwable) {
                Log.d("Guide", "GuideFailure")
<<<<<<< Updated upstream

=======
                Log.d("error", t.message.toString())
>>>>>>> Stashed changes
            }
        })
    }
    
    private fun getBookmark(){


    }


    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
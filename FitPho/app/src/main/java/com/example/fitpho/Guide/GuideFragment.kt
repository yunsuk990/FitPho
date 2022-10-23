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
import com.example.fitpho.NetworkModel.*
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentGuideBinding
import com.example.fitpho.util.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GuideFragment : Fragment() {

    private var _binding: FragmentGuideBinding? = null
    private val binding get() = _binding!!
    companion object{
        lateinit var prefs: SharedPreferenceUtil
    }


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
        prefs = SharedPreferenceUtil(requireContext())

        //구분선
        var dividerItemDecoration: DividerItemDecoration = DividerItemDecoration(view.context, 1)
        dividerItemDecoration.setDrawable(context?.resources!!.getDrawable(R.drawable.recyclerview_divider))
        binding.exList.addItemDecoration(dividerItemDecoration)


        binding.exList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.exList.adapter = guideAdapter

        guideAdapter.favoritesClickItem(object : GuideAdapter.favoritesClickListener{
            override fun onRemoveFavorites(position: Int) {
                guideAdapter.deleteFavorites(position)
            }
        })
        //가이드 부위별 운동

        binding.bookmark.setOnFocusChangeListener { v, hasFocus ->
            getFavorite()
        }
        binding.bookmark.setOnFocusChangeListener { v, hasFocus ->
            getFavorite()
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


    // 라이브러리 조회
    private fun getGuide(part: String){
        authService().guideData(part, prefs.getToken()!!)?.enqueue(object: Callback<GuideDataResponse>{
            override fun onResponse(
                call: Call<GuideDataResponse>,
                response: Response<GuideDataResponse>,
            ) {
                when(response.code()){
                    200 -> {
                        var res = response.body()
                        var data: ArrayList<data> = ArrayList(res?.getData())
                        var favorList: ArrayList<Int>? = res?.getFavorites()
                        guideAdapter.setGuideData(data,favorList)
                    }
                    else -> Log.d("Guide", "GuideFail")
                }
            }
            override fun onFailure(call: Call<GuideDataResponse>, t: Throwable) {
                Log.d("Guide", "GuideFailure")
                Log.d("error", t.message.toString())
            }
        })
    }


    //즐겨찾기 운동 가져오기
    private fun getFavorite(){
        authService().getFavorites(prefs.getToken()!!)?.enqueue(object: Callback<GetFavoritesResponse>{
            override fun onResponse(
                call: Call<GetFavoritesResponse>,
                response: Response<GetFavoritesResponse>
            ) {
                when(response.code()){
                    200 -> {
                        var res = response.body()
                        var data: ArrayList<data>? = ArrayList(res?.getFavorites())
                        guideAdapter.favoriteData(data)
                    }
                }
            }
            override fun onFailure(call: Call<GetFavoritesResponse>, t: Throwable) {
                Log.d("즐겨찾기 조회", t.message.toString())
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
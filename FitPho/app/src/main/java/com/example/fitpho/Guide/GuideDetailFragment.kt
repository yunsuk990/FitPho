package com.example.fitpho.Guide

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.GuideDetailResponse
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentGuideDetailBinding
import com.example.fitpho.util.SharedPreferenceUtil
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback
import kotlin.math.exp

class GuideDetailFragment : Fragment() {

    private var _binding: FragmentGuideDetailBinding? = null
    private val binding get() = _binding!!
    var id: Int? = 0
    var title: String? =""
    var img: String? = ""
    lateinit var explainlayout: LinearLayout
    companion object{
        lateinit var prefs: SharedPreferenceUtil
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGuideDetailBinding.inflate(inflater, container, false)
        explainlayout = binding.explain
        id = arguments?.getInt("id")
        title = arguments?.getString("title")
        img = arguments?.getString("img1")

        //툴바
        var mytoolbar  = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(mytoolbar)
        //뒤로가기 버튼
        //(activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.title.text = title
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = SharedPreferenceUtil(requireContext())

        //각 운동기구별 세부내용 조회

        authService().guideDetailData(id!!, prefs.getToken()!!).enqueue(object :retrofit2.Callback<GuideDetailResponse>{
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
                        Glide.with(requireContext()).load(res.getData()!![0].getStimulate2()).into(binding.stimulate2)
                        Glide.with(requireContext()).load(res.getData()!![0].getAnimation()).into(binding.animation)
                        binding.url.setOnClickListener(object : View.OnClickListener{
                            override fun onClick(p0: View?) {
                                var intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(res.getData()!![0].getUrl()))
                                startActivity(intent)
                            }

                        })

                        //운동설명
                        var s: String =""
                        for(i in 0..((text)?.minus(1)!!)){
                            createTextView(res.getText()[i], i+1)
                            Log.d("text", res.getText()[i])
                        }

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

    private fun createTextView(text: String?, i:Int){
        var textview: TextView = TextView(activity?.applicationContext)
        textview.text = "$i. $text"
        textview.textSize = 20F
        textview.setTextColor(Color.BLACK)
        var param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        param.topMargin = 30
        textview.layoutParams = param
        explainlayout.addView(textview)
    }

    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
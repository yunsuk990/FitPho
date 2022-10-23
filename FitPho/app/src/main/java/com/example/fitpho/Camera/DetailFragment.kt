package com.example.fitpho.Camera

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.GuideDetailResponse
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentDetailBinding
import com.example.fitpho.databinding.FragmentGuideDetailBinding
import com.example.fitpho.databinding.FragmentHomeBinding
import com.example.fitpho.util.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailFragment : Fragment() {


    private var _binding: FragmentGuideDetailBinding? = null
    private val binding get() = _binding!!
    private var id: Int? = 0
    companion object{
        lateinit var prefs: SharedPreferenceUtil
    }
    lateinit var explainlayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGuideDetailBinding.inflate(inflater, container, false)
        id = arguments?.getInt("id")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = SharedPreferenceUtil(requireContext())
        explainlayout = binding.explain


        Log.d("id", id.toString())
        authService().guideDetailData(id!!, prefs.getToken()!!).enqueue(object: Callback<GuideDetailResponse>{
            override fun onResponse(
                call: Call<GuideDetailResponse>,
                response: Response<GuideDetailResponse>,
            ) {
                when(response.code()){
                    200 -> {
                        var res = response.body()
                        var text = response.body()?.getText()?.size
                        binding.title.text = res?.getData()!![0].getTitle()
                        Glide.with(requireContext()).load(res?.getData()!![0].getStimulate1()).into(binding.stimulate1)
                        Glide.with(requireContext()).load(res?.getData()!![0].getStimulate2()).into(binding.stimulate2)
                        Glide.with(requireContext()).load(res?.getData()!![0].getAnimation()).into(binding.animation)
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
                        Log.d("Detail", "Fail1")
                    }
                }
            }
            override fun onFailure(call: Call<GuideDetailResponse>, t: Throwable) {
                Log.d("Detail", "FAILURE")
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
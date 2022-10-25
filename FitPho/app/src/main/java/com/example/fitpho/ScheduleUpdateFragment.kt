package com.example.fitpho

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.databinding.FragmentScheduleUpdateBinding
import com.example.fitpho.util.SharedPreferenceUtil

class ScheduleUpdateFragment : Fragment() {

    private var _binding: FragmentScheduleUpdateBinding? = null
    private val binding get() = _binding!!
    companion object{
        lateinit var prefs: SharedPreferenceUtil
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = SharedPreferenceUtil(requireContext())

        var date = arguments?.getString("date")
        var tvStart = arguments?.getString("tvStart")
        authService().DetailSchedule(date, tvStart, prefs.getToken())


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Retrofit api
    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }
}
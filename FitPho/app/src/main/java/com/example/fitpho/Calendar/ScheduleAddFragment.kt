package com.example.fitpho

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.FragmentActivity
import com.example.fitpho.databinding.FragmentScheduleAddBinding
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker
import java.text.SimpleDateFormat

class ScheduleAdd : Fragment() {

    private var _binding: FragmentScheduleAddBinding? = null
    private val binding get() = _binding!!


    private var mFormatter: SimpleDateFormat = SimpleDateFormat("MMMM dd yyyy hh:mm aa");
    //private var listener = SlideDateTimeListener()




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.textView3.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(p0: View?) {
//                SlideDateTimePicker.Builder(fragmentManager)
//                    .setListener(listener)
//            }
//
//        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}
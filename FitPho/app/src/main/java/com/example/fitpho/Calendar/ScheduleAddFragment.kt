package com.example.fitpho

import android.R.color
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.fitpho.databinding.DialogDatetimePickerBinding
import com.example.fitpho.databinding.FragmentScheduleAddBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class ScheduleAdd : Fragment(), TimePickerDialog.OnTimeSetListener {

    private var _binding: FragmentScheduleAddBinding? = null
    private val binding get() = _binding!!
    private var mformat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    var type: Int = 0

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

        binding.todayDate.text = getDate()
        binding.todayDate2.text = getDate()

        binding.container1.setOnClickListener{
            type = 0
            dialogTime()
        }
        binding.container2.setOnClickListener{
            type = 1
            dialogTime()
        }
        binding.btnCancel.setOnClickListener{
            findNavController().navigate(R.id.calenderFragment)
        }

        binding.btnSave.setOnClickListener{


        }
    }

    private fun dialogTime() {
        var calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        var minute = calendar.get(Calendar.MINUTE)
        var timePickerDialog = TimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, this , hour, minute, android.text.format.DateFormat.is24HourFormat(context))
        timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }

    private fun getDate(): String {
        var mdate: Date = Date(System.currentTimeMillis())
        var today = mformat.format(mdate)
        return today
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        if(type == 0){
            binding.startTime.text = String.format("%d 시 %d 분", p1, p2)
        }else{
            binding.endTime.text = String.format("%d 시 %d 분", p1, p2)
        }

    }

}

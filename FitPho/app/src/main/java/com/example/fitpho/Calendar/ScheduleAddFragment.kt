package com.example.fitpho

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TimePicker
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.CalendarSave
import com.example.fitpho.NetworkModel.CalendarSaveResponse
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.databinding.FragmentScheduleAddBinding
import com.example.fitpho.util.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ScheduleAdd : Fragment(), TimePickerDialog.OnTimeSetListener {

    private var _binding: FragmentScheduleAddBinding? = null
    private val binding get() = _binding!!
    private var mformat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    private var idformat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd:HH:mm:ss")
    var type: Int = 0
    var clickedDate: String = ""
    var startTime: String = ""
    var endTime: String = ""
    companion object{
        lateinit var prefs: SharedPreferenceUtil
    }
    var checkBoxes: ArrayList<CheckBox> = ArrayList()
    var tvTitle: String = ""

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
        prefs = SharedPreferenceUtil(requireContext())
        bindCheckbox()
        clickedDate = arguments?.getString("date", null)!!
        binding.todayDate.text = clickedDate
        binding.todayDate2.text = clickedDate
        for(i in 0 until checkBoxes.size){
            checkBoxes[i].setOnClickListener(clickListener)
        }

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
            var token = prefs.getToken()
            //현재 시,분,초 구하기
            var tv_Title: String = tvTitle.replaceFirstChar { "" }
            Log.d("tvtitle", tv_Title)
            var s = System.currentTimeMillis()
            var tvDate: String = getDate(s)
            Log.d("tvtitle", tvDate)
            var tvStart = startTime
            Log.d("tvtitle", tvStart)
            var tvEnd = endTime
            Log.d("tvtitle", tvEnd)
            var tvContent = binding.etMemo.text.toString()
            Log.d("tvtitle", tvContent)
            var schedule = CalendarSave(tv_Title, tvDate, tvStart, tvEnd, tvContent)
            //서버 요청
            getScheduleSave(tvDate, tvStart ,token!!, schedule)
        }
    }

    private var clickListener: View.OnClickListener = object : View.OnClickListener{
        override fun onClick(p0: View?) {
            when(p0?.id){
                R.id.checkbox_abs -> {
                    tvTitle += ","+binding.checkboxAbs.text
                }
                R.id.checkbox_arm -> {
                    tvTitle += ","+binding.checkboxArm.text
                }
                R.id.checkbox_back -> {
                    tvTitle += ","+binding.checkboxBack.text
                }
                R.id.checkbox_chest -> {
                    tvTitle += ","+binding.checkboxChest.text
                }
                R.id.checkbox_leg -> {
                    tvTitle += ","+binding.checkboxLeg.text
                }
                R.id.checkbox_shoulder -> {
                    tvTitle += ","+binding.checkboxShoulder.text
                }
                R.id.checkbox_other -> {
                    tvTitle += ","+binding.checkboxOther.text
                }
            }
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

    private fun getDate(date: Long): String {
        var date: Date = Date(date)
        var mformat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        var getTime = mformat.format(date)
        return getTime
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        if(type == 0){
            startTime = "$p1:$p2"
            binding.startTime.text = String.format("%d 시 %d 분", p1, p2)
        }else{
            endTime = "$p1:$p2"
            binding.endTime.text = String.format("%d 시 %d 분", p1, p2)
        }
    }

    private fun getCurrentTime(): Date {
        var id = Date(System.currentTimeMillis())
        var dat: String = idformat.format(id)
        return id
    }

    //private fun getTvTitle(): String {}

    private fun bindCheckbox(){
        checkBoxes.add(binding.checkboxChest)
        checkBoxes.add(binding.checkboxAbs)
        checkBoxes.add(binding.checkboxArm)
        checkBoxes.add(binding.checkboxBack)
        checkBoxes.add(binding.checkboxLeg)
        checkBoxes.add(binding.checkboxOther)
        checkBoxes.add(binding.checkboxShoulder)
    }

    private fun getScheduleSave(date: String, tvstart: String, token: String, schedule: CalendarSave) {
        authService().ScheduleSave(date, tvstart, token, schedule).enqueue(object : Callback<CalendarSaveResponse>{
            override fun onResponse(
                call: Call<CalendarSaveResponse>,
                response: Response<CalendarSaveResponse>
            ) {
                when(response.code()){
                    200 -> {
                        Log.d("일정 저장:", "성공" )
                        findNavController().navigate(R.id.calenderFragment)
                    }else -> {
                        Log.d("일정 저장:", "실패" )
                    }
                }
            }
            override fun onFailure(call: Call<CalendarSaveResponse>, t: Throwable) {
                Log.d("일정 저장:", "실패(통신오류)")
            }
        })
    }

    //Retrofit api
    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }
}

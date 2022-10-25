package com.example.fitpho

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TimePicker
import androidx.navigation.fragment.findNavController
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.*
import com.example.fitpho.databinding.FragmentScheduleUpdateBinding
import com.example.fitpho.util.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ScheduleUpdateFragment : Fragment(), TimePickerDialog.OnTimeSetListener {

    private var _binding: FragmentScheduleUpdateBinding? = null
    private val binding get() = _binding!!
    companion object{
        lateinit var prefs: SharedPreferenceUtil
    }
    var startTime: String = ""
    var endTime: String = ""
    var tvTitle: String = ""
    var type: Int = 0
    var checkBoxes: ArrayList<CheckBox> = ArrayList()

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
        bindCheckbox()

        var date = arguments?.getString("date")
        var tvStart = arguments?.getString("tvStart")

        for(i in 0 until checkBoxes.size){
            checkBoxes[i].setOnClickListener(clickListener)
        }



        authService().DetailSchedule(date!!, tvStart!!, prefs.getToken()!!).enqueue(object : Callback<CalendarDetailResponse>{
            override fun onResponse(
                call: Call<CalendarDetailResponse>,
                response: Response<CalendarDetailResponse>
            ) {

                when(response.code()){
                    200 -> {
                        var res = response.body()?.getData()
                        with(binding){
                            etMemo.setText(res?.get(0)!!.tvContent)
                            startTime.setText(res?.get(0)!!.tvStart)
                            endTime.setText(res?.get(0)!!.tvEnd)
                            todayDate.setText(res?.get(0)!!.tvDate)
                            todayDate2.setText(res?.get(0)!!.tvDate)
                        }
                        var title = res?.get(0)!!.tvTitle.split(",")
                        for(i in 0 until title.size) {
                            if (title[i] == binding.checkboxAbs.text) {
                                binding.checkboxAbs.isChecked = true
                            } else if (title[i] == binding.checkboxArm.text) {
                                binding.checkboxArm.isChecked = true
                            } else if (title[i] == binding.checkboxBack.text) {
                                binding.checkboxBack.isChecked = true
                            } else if (title[i] == binding.checkboxChest.text) {
                                binding.checkboxChest.isChecked = true
                            } else if (title[i] == binding.checkboxLeg.text) {
                                binding.checkboxLeg.isChecked = true
                            } else if (title[i] == binding.checkboxOther.text) {
                                binding.checkboxOther.isChecked = true
                            } else {
                                binding.checkboxShoulder.isChecked = true
                            }
                        }
                    }
                    400 -> {
                        Log.d("세부일정 수정", "실패")
                    }
                }
            }

            override fun onFailure(call: Call<CalendarDetailResponse>, t: Throwable) {
                Log.d("세부일정 수정", "실패(통신오류)")
            }
        })


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


            var tv_Title: String = tvTitle.replaceFirstChar { "" }
            Log.d("tvtitle", tv_Title)
            var s = System.currentTimeMillis()
            var tvDate: String = getDate(s)
            Log.d("tvtitle", tvDate)
            var tvstart = startTime
            Log.d("tvtitle", tvStart)
            var tvEnd = endTime
            Log.d("tvtitle", tvEnd)
            var tvContent = binding.etMemo.text.toString()
            Log.d("tvtitle", tvContent)
            var schedule = ScheduleUpdate(tv_Title, tvDate, tvstart, tvEnd, tvContent)
            //서버 요청
            getScheduleUpdate(date, tvStart ,token!!, schedule)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getScheduleUpdate(date: String, tvstart: String, token: String, schedule: ScheduleUpdate) {
        authService().ScheduleUpdate(date, tvstart, token, schedule).enqueue(object : Callback<CalendarUpdateResponse>{
            override fun onResponse(
                call: Call<CalendarUpdateResponse>,
                response: Response<CalendarUpdateResponse>
            ) {
                when(response.code()){
                    200 -> {
                        Log.d("일정 수정:", "성공" )
                        findNavController().navigate(R.id.calenderFragment)
                    }
                    else -> {
                        Log.d("일정 수정:", "실패")
                    }
                }
            }

            override fun onFailure(call: Call<CalendarUpdateResponse>, t: Throwable) {
                Log.d("일정 수정:", "실패(통신오류)" )
            }
        })
    }


    private fun dialogTime() {
        var calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        var minute = calendar.get(Calendar.MINUTE)
        var timePickerDialog = TimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, this , hour, minute, DateFormat.is24HourFormat(context))
        timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }

    //Retrofit api
    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }

    private fun getDate(date: Long): String {
        var date: Date = Date(date)
        var mformat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        var getTime = mformat.format(date)
        return getTime
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

    private fun bindCheckbox(){
        checkBoxes.add(binding.checkboxChest)
        checkBoxes.add(binding.checkboxAbs)
        checkBoxes.add(binding.checkboxArm)
        checkBoxes.add(binding.checkboxBack)
        checkBoxes.add(binding.checkboxLeg)
        checkBoxes.add(binding.checkboxOther)
        checkBoxes.add(binding.checkboxShoulder)
    }
}
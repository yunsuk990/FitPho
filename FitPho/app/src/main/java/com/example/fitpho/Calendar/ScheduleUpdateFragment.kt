package com.example.fitpho.Calendar

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
import com.example.fitpho.R
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
    var pastCheckbox: String = ""
    var currentCheckbox: String = ""

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

        //파라미터 값
        var dateParam = arguments?.getString("date")
        var tvStartParam = arguments?.getString("tvStart")

        for(i in 0 until checkBoxes.size){
            checkBoxes[i].setOnClickListener(clickListener)
        }


        //일정 조회 시 데이터 삽입
        scheduleVerify(dateParam!!, tvStartParam!!, prefs.getToken()!!)

        // -------------------------- 일정 수정할 경우
        // 시작시간 설정
        binding.container1.setOnClickListener{
            type = 0
            dialogTime()
        }

        // 마감시간 설정
        binding.container2.setOnClickListener{
            type = 1
            dialogTime()
        }

        // 취소 버튼
        binding.btnCancel.setOnClickListener{
            findNavController().navigate(R.id.calenderFragment)
        }

        // 저장 버튼
        binding.btnSave.setOnClickListener{
            var token = prefs.getToken()
            tvTitle = pastCheckbox + currentCheckbox

            var tv_Title: String = tvTitle.replaceFirstChar { "" }  //체크박스 값 가져오기
            Log.d("tvtitle", tv_Title)

            var s = System.currentTimeMillis()
            var tvDate: String = getDate(s)     // 현재 시간
            Log.d("tvtitle", tvDate)

            var tvStart: String? = startTime             //시작 시간
            Log.d("tvtitle", tvStart!!)

            var tvEnd: String? = endTime                 //마감 시간
            Log.d("tvtitle", tvEnd!!)

            var tvContent = binding.etMemo.text.toString()      //메모
            Log.d("tvtitle", tvContent)

            var schedule = ScheduleUpdate(tv_Title, tvDate, tvStart, tvEnd, tvContent)

            // 일정 수정 서버 요청
            getScheduleUpdate(dateParam, tvStartParam ,token!!, schedule)
        }


    }

    private fun scheduleVerify(date: String, tvStart: String, token: String) {
        authService().DetailSchedule(date!!, tvStart!!, prefs.getToken()!!).enqueue(object : Callback<CalendarDetailResponse>{
            override fun onResponse(
                call: Call<CalendarDetailResponse>,
                response: Response<CalendarDetailResponse>
            ) {

                when(response.code()){
                    200 -> {
                        var res = response.body()?.getData()
                        with(binding){
                            etMemo.setText(res?.get(0)!!.tvContent)     //메모
                            startTime.setText(res?.get(0)!!.tvStart)    //시작시간
                            endTime.setText(res?.get(0)!!.tvEnd)        //마감시간
                            todayDate.setText(res?.get(0)!!.tvDate)     //등록했던 날짜
                            todayDate2.setText(res?.get(0)!!.tvDate)    //등록했던 날짜
                        }

                        var title = res?.get(0)!!.tvTitle.split(",")
                        for(i in 0 until title.size) {
                            if (title[i] == binding.checkboxAbs.text) {
                                binding.checkboxAbs.isChecked = true
                                pastCheckbox += "," + binding.checkboxAbs.text
                            } else if (title[i] == binding.checkboxArm.text) {
                                binding.checkboxArm.isChecked = true
                                pastCheckbox += "," + binding.checkboxArm.text
                            } else if (title[i] == binding.checkboxBack.text) {
                                binding.checkboxBack.isChecked = true
                                pastCheckbox += "," + binding.checkboxBack.text
                            } else if (title[i] == binding.checkboxChest.text) {
                                binding.checkboxChest.isChecked = true
                                pastCheckbox += "," + binding.checkboxChest.text
                            } else if (title[i] == binding.checkboxLeg.text) {
                                binding.checkboxLeg.isChecked = true
                                pastCheckbox += "," + binding.checkboxLeg.text
                            } else if (title[i] == binding.checkboxOther.text) {
                                binding.checkboxOther.isChecked = true
                                pastCheckbox += "," + binding.checkboxOther.text
                            } else {
                                binding.checkboxShoulder.isChecked = true
                                pastCheckbox += "," + binding.checkboxShoulder.text
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
    }

    // 체크된 값 추가하기
    private var clickListener: View.OnClickListener = object : View.OnClickListener{
        override fun onClick(p0: View?) {
            when(p0?.id){
                R.id.checkbox_abs -> {
                    currentCheckbox += ","+binding.checkboxAbs.text
                }
                R.id.checkbox_arm -> {
                    currentCheckbox += ","+binding.checkboxArm.text
                }
                R.id.checkbox_back -> {
                    currentCheckbox += ","+binding.checkboxBack.text
                }
                R.id.checkbox_chest -> {
                    currentCheckbox += ","+binding.checkboxChest.text
                }
                R.id.checkbox_leg -> {
                    currentCheckbox += ","+binding.checkboxLeg.text
                }
                R.id.checkbox_shoulder -> {
                    currentCheckbox += ","+binding.checkboxShoulder.text
                }
                R.id.checkbox_other -> {
                    currentCheckbox += ","+binding.checkboxOther.text
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 일정 수정 API
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

    //다이얼로그 창 띄우기
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

    // 지정 포맷 형식으로 날짜 구하기
    private fun getDate(date: Long): String {
        var date: Date = Date(date)
        var mformat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        var getTime = mformat.format(date)
        return getTime
    }

    // 시작, 마감시간 가져와서 표시
    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        if(type == 0){
            startTime = "$p1:$p2"
            binding.startTime.text = String.format("%d 시 %d 분", p1, p2)
        }else{
            endTime = "$p1:$p2"
            binding.endTime.text = String.format("%d 시 %d 분", p1, p2)
        }
    }

    // 모든 체크박스 바인딩
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
package com.example.fitpho.Calendar

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.CalendarRequestResponse
import com.example.fitpho.NetworkModel.ScheduleGetDot
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.NetworkModel.schedule
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentCalenderBinding
import com.example.fitpho.util.SharedPreferenceUtil
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalenderFragment : Fragment() {

    private var _binding: FragmentCalenderBinding? = null
    private val binding get() = _binding!!
    private val scheduleAdapter by lazy { ScheduleAdapter(requireContext()) }
    private var mformat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    lateinit var clickedDay: String
    lateinit var calendar: MaterialCalendarView
    var day = CalendarDay.today()
    companion object{
        lateinit var prefs: SharedPreferenceUtil
    }
    var collect: ArrayList<CalendarDay> =  ArrayList<CalendarDay>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalenderBinding.inflate(inflater, container, false)
        var mytoolbar  = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(mytoolbar)

        calendar = binding.calendar
        //?????? ?????? ?????? ??????
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = SharedPreferenceUtil(requireContext())
        clickedDay = mformat.format(CalendarDay.today().date)
        getAllSchedule()
        CalendarInit()


        Log.d("CalendarDay.today",CalendarDay.today().toString())
        binding.rcvCalendar.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcvCalendar.adapter = scheduleAdapter

        // ?????? ?????? ?????? ???
        binding.verify.setOnClickListener {
            findNavController().navigate(R.id.scheduleAdd, Bundle().apply {
                putString("date", clickedDay)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun CalendarInit(){
        binding.calendar.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMaximumDate(CalendarDay.from(2030,11,31))
            .setMinimumDate(CalendarDay.from(2022,0,1))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()
        binding.calendar.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_month)))
        calendar.selectedDate = day
        var todayDecorator = TodayDecorator()
        binding.calendar.setTitleFormatter(DateFormatTitleFormatter(SimpleDateFormat("yyyy??? M???")))
        binding.calendar.setWeekDayTextAppearance(R.style.calendar_day)
        binding.calendar.setHeaderTextAppearance(R.style.calendar_title)
        binding.calendar.addDecorators(
            SundayDecorator(),
            SaturdayDecorator(),
            todayDecorator,
            DotDecorator(Color.RED, arrayListOf(CalendarDay.today()))
        )
        getSchedule()

        //?????? ??????
        binding.calendar.setOnDateChangedListener(object: OnDateSelectedListener{
            override fun onDateSelected(
                widget: MaterialCalendarView,
                date: CalendarDay,
                selected: Boolean
            ) {
                day = date
                clickedDay = mformat.format(date.date)
                Log.d("??????" , clickedDay)
                getSchedule()
            }
        })
    }


    //????????? ?????? ??????
    private fun getSchedule() {
        authService().ScheduleAdd(clickedDay, prefs.getToken()!!).enqueue(object : Callback<CalendarRequestResponse>{
            override fun onResponse(
                call: Call<CalendarRequestResponse>,
                response: Response<CalendarRequestResponse>
            ) {
                when(response.code()){
                    200 -> {
                        Log.d("????????? ?????? ??????", "??????")
                        var a = response.body()?.getData()
                        var data: ArrayList<schedule>? = response.body()?.getData()
//                                for( i in 0..a!!.size-1){
//                                    Log.d("????????? ?????? ??????", a[i].tvStart)
//                                    Log.d("????????? ?????? ??????", a[i].tvDate)
//                                    Log.d("????????? ?????? ??????", a[i].tvContent)
//                                    Log.d("????????? ?????? ??????", a[i].tvEnd)
//                                    Log.d("????????? ?????? ??????", a[i].tvTitle)
//                                }
                        scheduleAdapter.setCalendarList(data)
                    }
                    else -> {
                        Log.d("????????? ?????? ??????", "??????")
                    }
                }
            }
            override fun onFailure(call: Call<CalendarRequestResponse>, t: Throwable) {
                Log.d("????????? ?????? ??????", "??????(????????????")
            }
        })
    }

    //?????? ?????? ???????????? ?????? ?????????(input = "20221025")
    private fun getWeekDay(inputDate: String): String {
        var dateFormat = SimpleDateFormat("yyyyMMdd")
        var convertDate = dateFormat.parse(inputDate)
        var cal = Calendar.getInstance()
        cal.time = convertDate
        var weekday = cal.get(Calendar.DAY_OF_WEEK)
        //Log.d("????????????: ", cal.get(Calendar.DAY_OF_WEEK).toString())
        when(weekday){
            2 -> return "???"
            3 -> return "???"
            4 -> return "???"
            5 -> return "???"
            6 -> return "???"
            7 -> return "???"
            1 -> return "???"
        }
        return "??????"
    }

    private fun getAllSchedule() {

        authService().ScheduleGetDot(prefs.getToken()!!).enqueue(object : Callback<ScheduleGetDot>{
            override fun onResponse(
                call: Call<ScheduleGetDot>,
                response: Response<ScheduleGetDot>
            ) {
                when(response.code()){
                    200 -> {
                        Log.d("?????? Dot", "??????")
                        var dates = ArrayList<CalendarDay>()
                        var res = response.body()
                        var data = res?.getData()
                        var ca = Calendar.getInstance()
                        var calendar: CalendarDay = CalendarDay.from(ca)

                        for( i in 0 until data!!.size){
                            var cal = data[i].split("-")
                            var year: Int = Integer.parseInt(cal[0])
                            var month: Int = Integer.parseInt(cal[1])
                            var dayy: Int = Integer.parseInt(cal[2])
                            dates.add(calendar)
                            ca.set(year,month,dayy)
                        }
                        collect = dates
                        Log.d("?????? Dot ??????", collect.toString())
                    }
                    else -> {
                        Log.d("?????? Dot", "??????")
                    }
                }
            }

            override fun onFailure(call: Call<ScheduleGetDot>, t: Throwable) {
                Log.d("?????? Dot", "??????(????????????)")
            }

        })
    }

    //Retrofit api
    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }

}


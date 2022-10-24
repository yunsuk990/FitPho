package com.example.fitpho.Calendar

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentCalenderBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalenderFragment : Fragment() {

    private var _binding: FragmentCalenderBinding? = null
    private val binding get() = _binding!!
    lateinit var calendar: MaterialCalendarView
    private val scheduleAdapter by lazy { ScheduleAdapter(requireContext()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalenderBinding.inflate(inflater, container, false)
        var mytoolbar  = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(mytoolbar)

        calendar = binding.calendar
        //오늘 날짜 자동 선택
        calendar.selectedDate = CalendarDay.today()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CalendarInit()
//        var dividerItemDecoration: DividerItemDecoration = DividerItemDecoration(view.context, 1)
//        dividerItemDecoration.setDrawable(context?.resources!!.getDrawable(R.drawable.recyclerview_divider))
//        binding.rcvCalendar.addItemDecoration(dividerItemDecoration)


        binding.rcvCalendar.layoutManager = LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
        binding.rcvCalendar.adapter = scheduleAdapter

        var testlist = ArrayList<Item>()
        var a = Item(1, "안녕1", 2, "하이1")
        var a1 = Item(2, "안녕2", 3, "하이2")
        var a2 = Item(3, "안녕3", 4, "하이3")
        var a3 = Item(4, "안녕4", 5, "하이4")
        testlist.add(a)
        testlist.add(a1)
        testlist.add(a2)
        testlist.add(a3)

        scheduleAdapter.setSceduleList(testlist)


        binding.verify.setOnClickListener{
            findNavController().navigate(R.id.scheduleAdd)
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

        var todayDecorator = TodayDecorator()
        binding.calendar.setTitleFormatter(DateFormatTitleFormatter(SimpleDateFormat("yyyy년 M월")))
        binding.calendar.setWeekDayTextAppearance(R.style.calendar_day)
        binding.calendar.setHeaderTextAppearance(R.style.calendar_title)
        binding.calendar.addDecorators(
            SundayDecorator(),
            SaturdayDecorator(),
            todayDecorator,
            DotDecorator(Color.RED, Collections.singleton(CalendarDay.today()))
        )
    }
}


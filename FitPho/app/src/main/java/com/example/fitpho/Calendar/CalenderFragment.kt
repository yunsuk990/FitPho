package com.example.fitpho.Calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.fitpho.R
import com.example.fitpho.databinding.CalenderHeaderBinding
import com.example.fitpho.databinding.FragmentCalenderBinding
import com.example.fitpho.databinding.ItemBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class CalenderFragment : Fragment() {

    private var _binding: FragmentCalenderBinding? = null
    private val binding get() = _binding!!
    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalenderBinding.inflate(inflater, container, false)
        var mytoolbar  = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(mytoolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentMonth = YearMonth.now()
        val daysOfWeek = DayOfWeek.values()
        val firstDayOfWeek = arrayOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
        )

        with(binding.exTwoCalendar){
//            doOnPreDraw {
//                daySize = Size(binding.exTwoCalendar.width/7, 10)
//            }
            setup(YearMonth.now().minusMonths(8), YearMonth.now().plusMonths(3), firstDayOfWeek.first())
            scrollToMonth(currentMonth)
            itemAnimator = null

        }


        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val textView = ItemBinding.bind(view).exTwoDayText

            init {
                textView.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate == day.date) {
                            selectedDate = null
                            binding.exTwoCalendar.notifyDayChanged(day)
                        } else {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            binding.exTwoCalendar.notifyDateChanged(day.date)
                            oldDate?.let { binding.exTwoCalendar.notifyDateChanged(oldDate) }
                        }
                        selectedDate != null
                    }
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = CalenderHeaderBinding.bind(view).exTwoHeaderText
        }

        binding.exTwoCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                container.textView.text = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
            }
        }

        binding.exTwoCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)


            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    when (day.date) {
                        selectedDate -> {
                            textView.setTextColor(ContextCompat.getColor(requireContext(),
                                R.color.white))
                            textView.setBackgroundResource(R.drawable.outline)
                        }
                        today -> {
                            textView.setTextColor(ContextCompat.getColor(requireContext(),
                                R.color.red))
                            textView.background = null
                        }
                        else -> {
                            textView.setTextColor(ContextCompat.getColor(requireContext(),
                                R.color.white))
                            textView.background = null
                        }
                    }
                }else {
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.barcolor))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


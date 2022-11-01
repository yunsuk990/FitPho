package com.example.fitpho.Calendar

import android.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.util.*
import kotlin.collections.ArrayList

class DotDecorator: DayViewDecorator {
    private var color: Int
    private var dates: ArrayList<CalendarDay>

    constructor(color: Int, dates: ArrayList<CalendarDay>){
        this.color = color
        this.dates = dates
        Log.d("포함유뮤",dates.toString())
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(5F, color))
    }
}

package com.example.fitpho.Calendar

class Item(
    private var scheduleDay: Int,
    private var tvContent: String,
    private var tvTime:Int,
    private var tvTitle: String
)
{

    fun setScheduleDay(): Int{
        return scheduleDay
    }
    fun setTvContent(): String{
        return tvContent
    }
    fun setTvTIme(): Int{
        return tvTime
    }
    fun setTvTitle(): String{
        return tvTitle
    }
}
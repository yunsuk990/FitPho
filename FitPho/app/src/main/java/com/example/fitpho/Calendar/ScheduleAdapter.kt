package com.example.fitpho.Calendar

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.CalendarDeleteResponse
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.NetworkModel.schedule
import com.example.fitpho.R
import com.example.fitpho.databinding.LayoutRecyclerScheduleBinding
import com.example.fitpho.util.SharedPreferenceUtil
import com.prolificinteractive.materialcalendarview.CalendarDay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleAdapter(context: Context): RecyclerView.Adapter<ScheduleAdapter.ItemViewHolder>() {

    var scheduleList: ArrayList<schedule>? = ArrayList()
    companion object{
        val authService: API = getRetrofit().create(API::class.java)
    }
    var prefs: SharedPreferenceUtil = SharedPreferenceUtil(context)


    inner class ItemViewHolder(val binding: LayoutRecyclerScheduleBinding): RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = LayoutRecyclerScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var currentItem = scheduleList?.get(position)
        var date = currentItem?.tvDate //2022-10-26
        var sculp = currentItem?.tvDate.toString().split("-") //26 (일수)
        var tvstart = currentItem?.tvStart //15:30 (운동시작시간)
        holder.binding.scheduleDay.text = sculp[2]
        holder.binding.tvContent.text = currentItem?.tvContent
        holder.binding.tvTime.text = currentItem?.tvStart + " ~ " + currentItem?.tvEnd
        holder.binding.tvTitle.text = currentItem?.tvTitle
        holder.binding.btnEdit.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                Navigation.findNavController(p0!!).navigate(R.id.scheduleUpdateFragment , Bundle().apply {
                    putString("date", date)
                    putString("tvStart", tvstart)
                })
            }
        })
        holder.binding.btnRemove.setOnClickListener{
            deleteSchedule(date!!, tvstart!!, prefs.getToken()!!)
            scheduleList?.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = scheduleList?.size!!

    //데이터 초기화
    fun setCalendarList(scheduleData: ArrayList<schedule>?){
        scheduleList = scheduleData
        notifyDataSetChanged()
    }


    //일정 삭제
    fun deleteSchedule(date: String, tvStart: String, token: String) {

        authService.ScheduleDelete(date,tvStart, token).enqueue(object : Callback<CalendarDeleteResponse> {
            override fun onResponse(
                call: Call<CalendarDeleteResponse>,
                response: Response<CalendarDeleteResponse>
            ) {
                when(response.code()){
                    200 -> {
                        Log.d("일정 삭제", "완료")
                    }
                    else -> {
                        Log.d("일정 삭제", "실패")
                    }
                }
            }

            override fun onFailure(call: Call<CalendarDeleteResponse>, t: Throwable) {
                Log.d("일정 삭제", "실패(통신오류)")
            }

        })
    }




}
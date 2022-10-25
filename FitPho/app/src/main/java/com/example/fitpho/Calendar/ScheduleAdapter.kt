package com.example.fitpho.Calendar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.NetworkModel.schedule
import com.example.fitpho.R
import com.example.fitpho.databinding.LayoutRecyclerScheduleBinding

class ScheduleAdapter(): RecyclerView.Adapter<ScheduleAdapter.ItemViewHolder>() {

    var scheduleList: ArrayList<schedule>? = ArrayList()
    companion object{
        //lateinit var prefs: SharedPreferenceUtil
        val authService: API = getRetrofit().create(API::class.java)
    }


    inner class ItemViewHolder(val binding: LayoutRecyclerScheduleBinding): RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = LayoutRecyclerScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var currentItem = scheduleList?.get(position)
        var date = currentItem?.tvDate
        var sculp = currentItem?.tvDate.toString().split("-")[2]
        var tvstart = currentItem?.tvStart
        holder.binding.scheduleDay.text = sculp
        holder.binding.tvContent.text = currentItem?.tvContent
        holder.binding.tvTime.text = currentItem?.tvStart + "~" + currentItem?.tvEnd
        holder.binding.tvTitle.text = currentItem?.tvTitle
        holder.binding.btnEdit.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                Navigation.findNavController(p0!!).navigate(R.id.scheduleUpdateFragment , Bundle().apply {
                    putString("date", date)
                    putString("tvStart", tvstart)
                })
            }
        })
    }

    override fun getItemCount(): Int = scheduleList?.size!!

    fun setCalendarList(scheduleData: ArrayList<schedule>?){
        scheduleList = scheduleData
        notifyDataSetChanged()
    }


}
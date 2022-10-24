package com.example.fitpho.Calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitpho.NetworkModel.data
import com.example.fitpho.databinding.LayoutRecyclerScheduleBinding

class ScheduleAdapter(context: Context): RecyclerView.Adapter<ScheduleAdapter.ItemViewHolder>() {

    var scheduleList: ArrayList<Item>? = ArrayList()


    inner class ItemViewHolder(val binding: LayoutRecyclerScheduleBinding): RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = LayoutRecyclerScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var currentItem = scheduleList?.get(position)
        holder.binding.scheduleDay.text = currentItem?.setScheduleDay().toString()
        holder.binding.tvContent.text = currentItem?.setTvContent().toString()
        holder.binding.tvTime.text = currentItem?.setTvTIme().toString()
        holder.binding.tvTitle.text = currentItem?.setTvTitle().toString()
    }

    override fun getItemCount(): Int = scheduleList?.size!!

    fun setSceduleList(scheduleData: ArrayList<Item>){
        scheduleList = scheduleData
    }


}
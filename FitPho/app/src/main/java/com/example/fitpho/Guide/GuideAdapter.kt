package com.example.fitpho.Guide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitpho.Database.GuideEntity
import com.example.fitpho.databinding.ExlistItemBinding


class GuideAdapter(): RecyclerView.Adapter<ItemViewHolder>() {

    var guideList: List<GuideEntity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ExlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = guideList[position]
        holder.binding.textView.text = currentItem.title
    }

    override fun getItemCount() = guideList.size

    fun setData(guidedata: List<GuideEntity>){
        guideList = guidedata
        notifyDataSetChanged()
    }
}

class ItemViewHolder(val binding: ExlistItemBinding): RecyclerView.ViewHolder(binding.root) {}

package com.example.fitpho.Aimovement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.fitpho.NetworkModel.AiMoveData
import com.example.fitpho.R
import com.example.fitpho.databinding.AimoveexcerciesItemBinding

class MovementAdapter(context: Context): RecyclerView.Adapter<MovementAdapter.ViewHolder>() {

    var itemList: ArrayList<String> = ArrayList()
    interface AiMoveClickListener{
        fun itemClick(title: String)
    }


    private lateinit var mAimoveClickListener: AiMoveClickListener
    fun AimoveClickItem(itemClickLister: AiMoveClickListener){
        mAimoveClickListener = itemClickLister
    }

    class ViewHolder(val binding: AimoveexcerciesItemBinding): RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AimoveexcerciesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentItem = itemList?.get(position)
        var title = currentItem.toString()
        holder.binding.itemTitle.text = title
        holder.binding.itemTitle.setOnClickListener{
            mAimoveClickListener.itemClick(title)
        }
    }

    fun setitemList(data: ArrayList<String>){
        itemList = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = itemList.size
}

package com.example.fitpho.Guide

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitpho.NetworkModel.data
import com.example.fitpho.databinding.ExlistItemBinding


class GuideAdapter(private val context: Context): RecyclerView.Adapter<ItemViewHolder>() {

    var guideList: List<data> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ExlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = guideList[position]
        holder.binding.textView.text = currentItem.getTitle()
        Glide.with(context).load(currentItem.getImg1()).into(holder.binding.imageView)
        //holder.binding.imageView.setImageURI(currentItem.getImg1().toString())
    }

    override fun getItemCount() = guideList.size

    fun setAllData(guidedata: List<data>){
        guideList = guidedata
        notifyDataSetChanged()
    }

}

class ItemViewHolder(val binding: ExlistItemBinding): RecyclerView.ViewHolder(binding.root) {}

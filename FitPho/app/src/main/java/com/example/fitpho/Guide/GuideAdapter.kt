package com.example.fitpho.Guide

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitpho.MainActivity
import com.example.fitpho.NetworkModel.data
import com.example.fitpho.R
import com.example.fitpho.databinding.ExlistItemBinding


class GuideAdapter(private val context: Context): RecyclerView.Adapter<GuideAdapter.ItemViewHolder>() {

    var guideList: List<data> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ExlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = guideList[position]
        val title = currentItem.getTitle()
        val id = currentItem.getId()
        val img1 = currentItem.getImg1()
        holder.binding.textView.text = title
        Glide.with(context).load(currentItem.getImg1()).into(holder.binding.imageView)
        holder.binding.click.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Navigation.findNavController(v!!).navigate(R.id.guideDetailFragment,
                    Bundle().apply {
                        putString("title", title)
                        putInt("id", id)
                        putString("img1", img1)
                    })
            }
        })
        //holder.binding.imageView.setImageURI(currentItem.getImg1().toString())
    }

    override fun getItemCount() = guideList.size

    fun setAllData(guidedata: List<data>){
        guideList = guidedata
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(val binding: ExlistItemBinding): RecyclerView.ViewHolder(binding.root) {}

}


package com.example.fitpho.Guide

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.*
import com.example.fitpho.R
import com.example.fitpho.databinding.ExlistItemBinding
import com.example.fitpho.util.SharedPreferenceUtil
import retrofit2.Call
import retrofit2.Response


class GuideAdapter(private val context: Context): RecyclerView.Adapter<GuideAdapter.ItemViewHolder>() {

    var prefs: SharedPreferenceUtil = SharedPreferenceUtil(context)
    var guideList: List<data>? = ArrayList()
    private var favoritesCheck: Boolean = false

    fun setFavoritesCheck(type: Boolean){
        this.favoritesCheck = type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ExlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = guideList?.get(position)
        var code = false
        val title = currentItem?.getTitle()
        val id = currentItem?.getId()
        val img1 = currentItem?.getImg1()
        holder.binding.textView.text = title
        Glide.with(context).load(currentItem?.getImg1()).placeholder(R.drawable.star).apply(RequestOptions.bitmapTransform(
            RoundedCorners(25))).into(holder.binding.imageView)
        if(favoritesCheck){
            code = true
            holder.binding.imageButton.setBackgroundResource(R.drawable.bookmark_white)
        }

        //부위 세부사항
        holder.binding.click.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Navigation.findNavController(v!!).navigate(R.id.guideDetailFragment,
                    Bundle().apply {
                        putString("title", title)
                        putInt("id", id!!)
                        putString("img1", img1)
                    })
            }
        })

        //즐겨찾기 추가/삭제 기능
        holder.binding.imageButton.setOnClickListener{
            var token = prefs.getToken()
            if(!code){
                //즐겨찾기 추가
                it.setBackgroundResource(R.drawable.bookmark_white)
                authService().addFavorites(id!!, token!!).enqueue(object: retrofit2.Callback<GetAddFavoritesResponse>{
                    override fun onResponse(
                        call: Call<GetAddFavoritesResponse>,
                        response: Response<GetAddFavoritesResponse>
                    ) {
                        when(response.code()){
                            200 -> {
                                Log.d("즐겨찾기 추가", "성공")
                            }
                            else -> {
                                Log.d("즐겨찾기 추가", "실패")
                            }
                        }
                    }
                    override fun onFailure(call: Call<GetAddFavoritesResponse>, t: Throwable) {
                        Toast.makeText(context, "즐겨찾기 추가 오류", Toast.LENGTH_SHORT).show()
                    }

                })
                code = true
            }else{
                //즐겨찾기 삭제
                it.setBackgroundResource(R.drawable.bookmark_black_small)
                authService().deleteFavorites(id!!, token!!).enqueue(object: retrofit2.Callback<GetDeleteFavoritesResponse>{
                    override fun onResponse(
                        call: Call<GetDeleteFavoritesResponse>,
                        response: Response<GetDeleteFavoritesResponse>
                    ) {
                        when(response.code()){
                            200 -> {
                                Log.d("즐겨찾기 삭제", "성공")
                                notifyDataSetChanged()
                            }
                            else -> {
                                Log.d("즐겨찾기 삭제", "실패")
                            }
                        }
                    }
                    override fun onFailure(call: Call<GetDeleteFavoritesResponse>, t: Throwable) {
                        Toast.makeText(context, "즐겨찾기 삭제 오류", Toast.LENGTH_SHORT).show()                    }

                })
                code = false
            }

        }
    }

    override fun getItemCount(): Int = guideList?.size!!

    //가이드 부위별 운동 나열
    fun setGuideData(guidedata: List<data>){
        setFavoritesCheck(false)
        guideList = guidedata
        notifyDataSetChanged()
    }

    //즐겨찾기 운동 나열
    fun favoriteData(favorite: List<data>?){
        setFavoritesCheck(true)
        guideList = favorite
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(val binding: ExlistItemBinding): RecyclerView.ViewHolder(binding.root) {}

}

private fun authService(): API {
    return getRetrofit().create(API::class.java)
}


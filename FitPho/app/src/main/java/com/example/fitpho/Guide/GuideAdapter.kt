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
    var guideList: ArrayList<data>? = ArrayList()
    var favoriteList: ArrayList<Int>? = ArrayList()
    private var favoritesCheck: Boolean = false

    interface favoritesClickListener{
        fun onRemoveFavorites(position: Int)
    }
    private lateinit var mfavoritesClickListener: favoritesClickListener
    fun favoritesClickItem(itemClickLister: favoritesClickListener){
        mfavoritesClickListener = itemClickLister
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ExlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = guideList?.get(position)
        val title = currentItem?.getTitle()
        val id = currentItem?.getId()
        val img1 = currentItem?.getImg1()

        holder.binding.textView.text = title
        Glide.with(context).load(currentItem?.getImg1()).placeholder(R.drawable.star).apply(RequestOptions.bitmapTransform(
            RoundedCorners(25))).into(holder.binding.imageView)

        //즐겨찾기 화면일시
        if(favoritesCheck){
            holder.binding.imageButton.setBackgroundResource(R.drawable.bookmark_white)
            holder.binding.imageButton.setOnClickListener{
                mfavoritesClickListener.onRemoveFavorites(position)
                deleteFavoriteService(id!!)
            }
        }else{ //부위별 조회 화면 일시
            if (favoriteList?.contains(id!!) == true){
                holder.binding.imageButton.setBackgroundResource(R.drawable.bookmark_white)
            }else{
                holder.binding.imageButton.setBackgroundResource(R.drawable.bookmark_black_small)
            }
            holder.binding.imageButton.setOnClickListener {
                if (favoriteList?.contains(id!!) == true) {
                    it.setBackgroundResource(R.drawable.bookmark_black_small)
                    deleteFavoriteService(id!!)
                }else{
                    it.setBackgroundResource(R.drawable.bookmark_white)
                    addFavoriteService(id!!)
                }
            }
        }

        //부위 세부사항으로 이동
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
    }

    override fun getItemCount(): Int = guideList?.size!!

    inner class ItemViewHolder(val binding: ExlistItemBinding): RecyclerView.ViewHolder(binding.root) {}


    //가이드 부위별 운동 나열
    fun setGuideData(guidedata: ArrayList<data>, favoriteData: ArrayList<Int>?){
        guideList?.clear()
        favoriteList?.clear()
        favoriteList = favoriteData
        guideList = guidedata
        notifyDataSetChanged()
        setFavoritesCheck(false)
    }

    //즐겨찾기 운동 나열
    fun favoriteData(favorite: ArrayList<data>?){
        guideList?.clear()
        guideList = favorite
        notifyDataSetChanged()
        setFavoritesCheck(true)
    }

    //즐겨찾기 항목 삭제
    fun deleteFavorites(position: Int){
        guideList?.removeAt(position)
        notifyDataSetChanged()
    }

    //즐겨찾기 또는 부위별 조회인지 구분
    fun setFavoritesCheck(type: Boolean){
        favoritesCheck = type
    }

    //즐겨찾기추가 Server로 항목 보내기
    fun addFavoriteService(id: Int){
        authService().addFavorites(id!!, prefs.getToken()!!).enqueue(object: retrofit2.Callback<GetAddFavoritesResponse>{
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
    }

    //즐겨찾기삭제 Server로 항목 보내기
    fun deleteFavoriteService(id: Int){
        authService().deleteFavorites(id!!, prefs.getToken()!!).enqueue(object: retrofit2.Callback<GetDeleteFavoritesResponse>{
            override fun onResponse(
                call: Call<GetDeleteFavoritesResponse>,
                response: Response<GetDeleteFavoritesResponse>
            ) {
                when(response.code()){
                    200 -> {
                        Log.d("즐겨찾기 삭제", "성공")
                    }
                    else -> {
                        Log.d("즐겨찾기 삭제", "실패")
                    }
                }
            }
            override fun onFailure(call: Call<GetDeleteFavoritesResponse>, t: Throwable) {
                Toast.makeText(context, "즐겨찾기 삭제 오류", Toast.LENGTH_SHORT).show()                    }

        })
    }

}

private fun authService(): API {
    return getRetrofit().create(API::class.java)
}


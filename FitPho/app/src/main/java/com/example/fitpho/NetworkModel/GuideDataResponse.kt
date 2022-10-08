package com.example.fitpho.NetworkModel

class GuideDataResponse(
    private val success: String,
    private val message: String,
    private val data: ArrayList<data>
) {
    public fun getSuccess(): String{
        return success
    }
    public fun getMessage(): String{
        return message
    }
    public fun getData(): ArrayList<data> {
        return data
    }
}

class data(
    private val id: Int,
    private val title: String,
    private val img1: String,
    private val img2: String
){
    fun getId(): Int{
        return id
    }
    fun getTitle(): String{
        return title
    }
    fun getImg1(): String{
        return img1
    }
    fun getImg2(): String{
        return img2
    }
}
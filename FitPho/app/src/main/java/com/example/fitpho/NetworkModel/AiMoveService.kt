package com.example.fitpho.NetworkModel


class AiMoveResponse(
    private val success: String,
    private val message: String,
    private val data: ArrayList<AiMoveData>
) {
    fun getMessage(): String{
        return message
    }
    fun getData(): ArrayList<AiMoveData>{
        return data
    }
}

class AiMoveData(
    private var id: Int,
    private var title: String
){
    fun getTitle(): String{
        return title
    }

    fun getId(): Int{
        return id
    }
}
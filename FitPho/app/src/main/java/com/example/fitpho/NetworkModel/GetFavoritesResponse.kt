package com.example.fitpho.NetworkModel

//즐겨찾기 조회 응답
class GetFavoritesResponse(
    private val success: String?,
    private val message: String?,
    private val favorites: ArrayList<data>?
) {

    fun getSuccess(): String? {
        return success
    }
    fun getMessage(): String? {
        return message
    }
    fun getFavorites(): ArrayList<data>? {
        return favorites
    }
}

class favoritesList(
    private val id: Int?,
    private val title: String?,
    private val img1: String?,
){
    fun getId(): Int?{
        return id
    }
    fun getTitle(): String?{
        return title
    }
    fun getImg1(): String?{
        return img1
    }
}





//즐겨찾기 추가 응답
class GetAddFavoritesResponse(
    private val success: String?,
    private val message: String?
){
    fun getSuccess(): String? {
        return success
    }
    fun getMessage(): String? {
        return message
    }
}

//즐겨찾기 삭제 응답
class GetDeleteFavoritesResponse(
    private val success: String?,
    private val message: String?
){
    fun getSuccess(): String? {
        return success
    }
    fun getMessage(): String? {
        return message
    }
}
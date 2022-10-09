package com.example.fitpho.NetworkModel

// 부위별 라이브러리 조회
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




// 각 운동 기구 별 세부내용 조회
class GuideDetailResponse(
    private val success: String,
    private val message: String,
    private val data: Array<detailData>
) {

    fun getSuccess(): String {
        return success
    }
    fun getMessage(): String {
        return message
    }
    fun getData(): Array<detailData> {
        return data
    }

}

class detailData(
    private val id: Int,
    private val stimulate1: String,
    private val stimulate2: String,
    private val text: String,
    private val animation: String
){
    fun getId(): Int {
        return id
    }
    fun getStimulate1(): String {
        return stimulate1
    }
    fun getStimulate2(): String {
        return stimulate2
    }
    fun getText(): String {
        return text
    }
    fun getAnimation(): String {
        return animation
    }
}

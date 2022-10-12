package com.example.ai

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ColorSpace
import android.media.ThumbnailUtils
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.renderscript.Element
import android.util.Log
import android.view.LayoutInflater
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.ai.databinding.ActivityMainBinding
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MainActivity : AppCompatActivity() {


    var id: Int? = 0
    var imageSize: Int = 224
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)






    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 카메라로 찍은 사진 가져오기
        if(requestCode == 1 && resultCode == RESULT_OK) {
            var image: Bitmap = data?.extras?.get("data") as Bitmap
            var dimension: Int = Math.min(image.width, image.height)
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
            binding.galleryResult.setImageBitmap(image)
            image = Bitmap.createScaledBitmap(image , imageSize, imageSize, false)
            classifyImage(image)
        }else {
            // 갤러리에서 사진 가져오기
            var dat: Uri = data?.getData()!!
            var image: Bitmap? = null
            try{
                image = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, dat)
            }catch (e: IOException){
                e.printStackTrace()
            }
            binding.galleryResult.setImageBitmap(image)
            image = Bitmap.createScaledBitmap(image!!, imageSize, imageSize, false)
            classifyImage(image)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun classifyImage(image: Bitmap?) {

        val model = ColorSpace.Model.newInstance(requireContext())
// Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), Element.DataType.FLOAT32)

        var byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4*imageSize*imageSize*3)
        byteBuffer.order(ByteOrder.nativeOrder())

        var intValues = IntArray(imageSize*imageSize)
        image?.getPixels(intValues, 0, image.width, 0,0,image.width-1, image.height-1)
        var pixel:Int = 0
        for(i in 0 until imageSize){
            for(j in 0 until imageSize){
                val i: Int = intValues[pixel++] //RGB
                byteBuffer.putFloat( (((i.shr(16)))and((0xFF)))*(1.0f/ 255.0f))
                byteBuffer.putFloat((( i.shr(8))and(0xFF))*(1.0f/ 255.0f))
                byteBuffer.putFloat((i and (0xFF))*(1.0f/255.0f))
            }
        }
        inputFeature0.loadBuffer(byteBuffer)

// Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray

        var maxconfidences = 0.0F
        var maxPos = 0

        for(i in 0 until confidences?.size!!){
            if(confidences[i] > maxconfidences){
                maxconfidences = confidences[i]
                maxPos = i
            }
        }

        val classes: Array<Int> = arrayOf(1,2,3,4)
        //val classes: Array<Int> = arrayOf(1,2,3,4,9,10,11,12,18,19,20,22,23,24,25,26,27,28,29,30,38,42,43)

        var s: String? = ""
        Log.d("classSize", classes.size.toString())

        //모든 항목 예측치 출력
        for(i in 0 until classes.size){
            s+= String.format("%s: %.3f%%\n", classes[i], confidences[i]*100)
        }
        Log.d("Result", s!!)
        model.close()
    }
}
package com.example.fitpho.Camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.fitpho.R
import com.example.fitpho.databinding.DialogSelectBinding
import com.example.fitpho.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class SelectDialog: DialogFragment() {

    private lateinit var binding: DialogSelectBinding
    var imageSize: Int = 224
    var image: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.camera.setOnClickListener {
            if (requireActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                var cameraIntent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 1)
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }

        binding.gallery.setOnClickListener {
            var cameraIntent: Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(cameraIntent, 3)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 카메라로 찍은 사진 가져오기
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            image= data?.extras?.get("data") as Bitmap
            var dimension: Int = Math.min(image!!.width, image!!.height)
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
            image = Bitmap.createScaledBitmap(image!! , imageSize, imageSize, false)
            classifyImage(image)

        }else {
            // 갤러리에서 사진 가져오기
            var dat: Uri = data?.getData()!!
            image = null
            try{
                image = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, dat)
            }catch (e: IOException){
                e.printStackTrace()
            }
            image = Bitmap.createScaledBitmap(image!!, imageSize, imageSize, false)
            classifyImage(image)

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun classifyImage(image: Bitmap?) {

        val model = Model.newInstance(requireActivity().applicationContext)
// Creates inputs for reference.

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        var byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4*imageSize*imageSize*3)
        byteBuffer.order(ByteOrder.nativeOrder())

        var intValues = IntArray(imageSize*imageSize)
        image?.getPixels(intValues, 0, image.width, 0,0,image.width, image.height)
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

        //val classes: Array<Int> = arrayOf(1,2,3,4)
        val classes: Array<Int> = arrayOf(1,2,3,4,9,10,11,12,18,19,20,22,23,24,25,26,27,28,29,30,38,42,43)
        //val classes: Array<Int> = arrayOf(1,2,3,4)

        var s: String? = ""
        Log.d("classSize", classes.size.toString())

        //모든 항목 예측치 출력
        for(i in 0 until classes.size){
            s+= String.format("%s: %.3f%%\n", classes[i], confidences[i]*100)
        }
        var act = confidences[maxPos]*100
        Log.d("Result", s!!) // 결과값 출력
        Log.d("매칭운동", classes[maxPos].toString())
        Log.d("ㅇㅇㅇㅇㅇㅇㅇ", act.toString())



        //매칭된 운동 아이디
        var id: Int? = classes[maxPos]

        // 예측률 이상일 경우에만
        if( act > 80){
            //Test
            findNavController().navigate( R.id.ai_ImageViewFragment ,
                Bundle().apply {
                    putInt("id", id!!)
                    putParcelable("image", image)
                })
        }else{
            Toast.makeText(requireContext(), "인식하지를 못하였습니다.", Toast.LENGTH_LONG)
            findNavController().navigate(R.id.homeFragment)
        }

// Releases model resources if no longer used.
        model.close()
    }

}
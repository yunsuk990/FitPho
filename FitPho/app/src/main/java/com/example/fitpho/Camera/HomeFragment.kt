package com.example.fitpho.Camera

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fitpho.Network.API
import com.example.fitpho.NetworkModel.GuideDetailResponse
import com.example.fitpho.NetworkModel.data
import com.example.fitpho.NetworkModel.getRetrofit
import com.example.fitpho.R
import com.example.fitpho.databinding.FragmentHomeBinding
import com.example.fitpho.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var id: Int? = 0
    var imageSize: Int = 224


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val mytoolbar  = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(mytoolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnCamera.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(requireActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    var cameraIntent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, 1)
                }else{
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
                }
            }
        })


        binding.gallery.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var cameraIntent: Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(cameraIntent, 3)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            // 카메라로 찍은 사진 가져오기
        if(requestCode == 1 && resultCode == RESULT_OK) {
            var image: Bitmap = data?.extras?.get("data") as Bitmap
            var dimension: Int = Math.min(image.width, image.height)
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
            binding.galleryResult.setImageBitmap(image)
            image = Bitmap.createScaledBitmap(image , imageSize, imageSize, false)
            //classifyImage(image)
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
            //classifyImage(image)
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

        //결과값 출력
        Log.d("Result", s!!)
       // Log.d("매칭운동", classes[maxPos].toString())

        //매칭된 운동 아이디
        //id = classes[maxPos]
        //Log.d("매칭운동", id.toString())




        //Test
//        findNavController().navigate(R.id.detailFragment,
//            Bundle().apply {
//                putInt("id", id!!)
//            })

//        예측치가 일정수치를 넘을 시 제약사항
//        if(confidences[maxPos].toInt() > 50){
//
//        }else{
//
//        }

// Releases model resources if no longer used.
        model.close()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun authService(): API {
        return getRetrofit().create(API::class.java)
    }
}
package com.example.fitpho.Camera

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
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
import androidx.fragment.app.Fragment
import com.example.fitpho.databinding.FragmentHomeBinding
import com.example.fitpho.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var resultLauncher : ActivityResultLauncher<Intent>
    lateinit var filePath: String
    val REQUEST_IMAGE_CAPTURE = 1

    var imageSize = 224


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        var mytoolbar  = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(mytoolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnCamera.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
        }
    }

    private fun checkPermission() {
        var permission = mutableMapOf<String, String>()
        permission["camera"] = Manifest.permission.CAMERA
        var denied = permission.count { ContextCompat.checkSelfPermission(requireContext(), it.value)  == PackageManager.PERMISSION_DENIED }
        if(denied > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission.values.toTypedArray(), REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_IMAGE_CAPTURE) {
            var count = grantResults.count { it == PackageManager.PERMISSION_DENIED }

            if(count != 0) {
                Toast.makeText(requireContext(), "권한을 동의해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            var image: Bitmap = data?.extras?.get("data") as Bitmap
            var dimension: Int = Math.min(image.width, image.height)
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
            binding.galleryResult.setImageBitmap(image)
            image = Bitmap.createScaledBitmap(image , imageSize, imageSize, false)
            classifyImage(image)
        }
    }

    private fun classifyImage(image: Bitmap?) {

        val model = Model.newInstance(requireContext())
// Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)

        var byteBuffer = ByteBuffer.allocateDirect(4*imageSize*imageSize*3)
        byteBuffer.order(ByteOrder.nativeOrder())

        var intValues: Array<Int> = Array(imageSize*imageSize)
        image?.getPixels(intValues, 0, image.width, 0,0,image.width, image.height)
        var pixel = 0
        for(i in 0 until imageSize){
            for(j in 0 until imageSize){
                val i: Int? = intValues[pixel++] //RGB
                byteBuffer.putFloat(((( i>>16)&0xFF)*(1.f/255.f)))
                byteBuffer.putFloat((( i>>8)&0xFF)*(1.f/255.f))
                byteBuffer.putFloat(( i>>0xFF)*(1.f/255.f))
            }
        }
        inputFeature0.loadBuffer(byteBuffer)

// Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        var confidences: IntArray? = outputFeature0.intArray
        var maxconfidences = 0
        var maxPos = 0
        for(i in 0 until confidences?.size!!){
            if(confidences[i] > maxconfidences){
                maxconfidences = confidences[i]
                maxPos = i
            }
        }

        var classes: Array<Int> = arrayOf(0,1,2,3,4,9,10,11,12,18,19,20,23,24,25,26,27,28,29,30,31,39,43,44)
        Log.d("Result", classes[maxPos].toString())


// Releases model resources if no longer used.
        model.close()

    }
}
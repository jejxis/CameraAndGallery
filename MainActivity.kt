package com.example.cameraandgallery

import android.Manifest
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.cameraandgallery.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    lateinit var cameraPermission:ActivityResultLauncher<String>//카메라 권한..런처의 컨트랙트로 RequestPermission 사용 -> String
    lateinit var storagePermission:ActivityResultLauncher<String>//저장소 권한..런처의 컨트랙트로 RequestPermission 사용 -> String
    lateinit var cameraLauncher: ActivityResultLauncher<Uri>//카메라 앱 호출..TakePicture 사용 -> Uri
    lateinit var galleryLauncher: ActivityResultLauncher<String>

    var photoUri:Uri? = null//사진 정보
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        storagePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
            if(isGranted){
                setViews()
            }else{
                Toast.makeText(baseContext, "외부 저장소 권한을 승인해야 앱을 사용할 수 있습니다.",
                Toast.LENGTH_LONG).show()
                finish()
            }
        }
        cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                openCamera()
            }else{
                Toast.makeText(baseContext, "카메라 권한을 승인해야 카메라를 사용할 수 있습니다.",
                    Toast.LENGTH_LONG).show()
                finish()
            }
        }
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){
            isSuccess ->
                if(isSuccess){
                    binding.imagePreview.setImageURI(photoUri)
                }
        }
        storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
            binding.imagePreview.setImageURI(uri)
        }
    }
    fun setViews(){//외부 저장소 권한이 승인 되었을 때 호출
        binding.buttonCamera.setOnClickListener {
            cameraPermission.launch(Manifest.permission.CAMERA)
        }
        binding.buttonGallery.setOnClickListener {
            openGallery()
        }
    }
    fun openCamera(){//카메라 요청
        val photoFile = File.createTempFile(
            "IMG_",
            ".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
        photoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            photoFile
        )
        cameraLauncher.launch(photoUri)//카메라 호출
    }
    fun openGallery(){
        galleryLauncher.launch("image/*")
    }
}
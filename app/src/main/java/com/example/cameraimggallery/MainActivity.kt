package com.example.cameraimggallery

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
        private const val EXTERNAL_PERMISSION_REQUEST_CODE = 1002
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCamera: Button = findViewById(R.id.btnCamera);
        val btnGallery: Button = findViewById(R.id.btnGallery);

        btnCamera.setOnClickListener{
            // Si els permisos de càmera no estan validats
            if (!isCameraPermissionGranted()) {
                // Farem una petició de permisos
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                // Sinó farem l'intent de mostrar la càmera
                cameraResult.launch(Intent(ACTION_IMAGE_CAPTURE));
            }
        }

        btnGallery.setOnClickListener{
            // Si els permisos d'accedir a fitxers externs no estan validats
            if (!isExternalPermissionGranted()) {
                // Farem una petició de permisos
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    EXTERNAL_PERMISSION_REQUEST_CODE
                )
            } else {
                // Sinó farem l'intent d'obrir la galeria
                val intent =Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                externalResult.launch(intent)
            }
        }
    }

    // Resposta de la càmera
    private val cameraResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val intent = result.data;
            val imageBitmap = intent?.extras?.get("data") as Bitmap;
            val imageView:ImageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(imageBitmap);
        }
    }

    // Resposta de la galeria
    private val externalResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        if (result.resultCode === Activity.RESULT_OK && result.data != null) {
            val photoUri = result.data!!.data
            val imageView:ImageView = findViewById(R.id.imageView);
            imageView.setImageURI(photoUri);
        }
    }

    // Permisos camera photo
    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    //Permisos external storage
    private fun isExternalPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    // Resposta a l'acció de l'usuari en validar o no els permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with opening camera
            } else {
                // Permission denied, handle accordingly
            }
        }else if(requestCode == EXTERNAL_PERMISSION_REQUEST_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with opening camera
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

}
package com.biturver.app.feature.license.presentation

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.biturver.app.R

class LicenseScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_license_scan)

        setupUI()
        startScanningAnimation()
    }

    private fun setupUI() {
        findViewById<View>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }
        
        findViewById<View>(R.id.btnCapture).setOnClickListener {
            if (checkCameraPermission()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        return androidx.core.content.ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        androidx.core.app.ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA), 100
        )
    }

    private fun openCamera() {
        val intent = android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, 101)
        } catch (e: Exception) {
            android.widget.Toast.makeText(this, "Kamera başlatılamadı: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {
             val imageBitmap = data?.extras?.get("data") as? android.graphics.Bitmap
             if (imageBitmap != null) {
                 findViewById<ImageView>(R.id.imgLicensePreview).setImageBitmap(imageBitmap)
                 findViewById<ImageView>(R.id.imgLicensePreview).alpha = 1.0f
                 
                 // Show success
                 android.widget.Toast.makeText(this, "Ehliyet Görseli Alındı!", android.widget.Toast.LENGTH_SHORT).show()
             }
        }
    }

    private fun startScanningAnimation() {
        val scanLine = findViewById<View>(R.id.scanLine)
        val container = scanLine.parent as View
        
        container.post {
            val height = container.height.toFloat()
            val animation = ObjectAnimator.ofFloat(scanLine, "translationY", 0f, height)
            animation.duration = 2000
            animation.repeatCount = ObjectAnimator.INFINITE
            animation.repeatMode = ObjectAnimator.REVERSE
            animation.interpolator = LinearInterpolator()
            animation.start()
        }
    }
}
package com.biturver.app.feature.damage.presentation

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import android.widget.Toast
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.biturver.app.R
import com.biturver.app.data.local.VeritabaniYardimcisi
import com.google.android.material.card.MaterialCardView
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DamageDetectionActivity : AppCompatActivity() {

    private lateinit var btnFotoCek: View
    private lateinit var btnGaleri: View
    private lateinit var btnBack: View
    private lateinit var imgHasar: ImageView
    private lateinit var txtSonuc: TextView
    private lateinit var txtDescription: TextView
    private lateinit var tvStatus: TextView
    private lateinit var viewScanLine: View
    private lateinit var cardResult: MaterialCardView
    private lateinit var cardImageContainer: View

    // Timeline Views
    private lateinit var tvStep1: TextView
    private lateinit var tvStep2: TextView
    private lateinit var tvStep3: TextView

    private val MODEL_NAME = "model.tflite"
    private val IMAGE_SIZE = 224

    private val CAMERA_REQUEST = 123
    private val GALLERY_REQUEST = 456

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_damage_detection)

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        btnFotoCek = findViewById(R.id.btnFotoCek)
        btnGaleri = findViewById(R.id.btnGaleri)
        btnBack = findViewById(R.id.btnBack) // Ensure this exists in XML
        imgHasar = findViewById(R.id.imgHasarFoto)
        
        // Find views, some might be inside layouts, so direct find works if IDs are unique
        txtSonuc = findViewById(R.id.txtSonuc)
        txtDescription = findViewById(R.id.txtDescription)
        tvStatus = findViewById(R.id.tvStatus)
        viewScanLine = findViewById(R.id.viewScanLine)
        cardResult = findViewById(R.id.cardResult)
        cardImageContainer = findViewById(R.id.cardImageContainer)

        tvStep1 = findViewById(R.id.tvStep1)
        tvStep2 = findViewById(R.id.tvStep2)
        tvStep3 = findViewById(R.id.tvStep3)
    }

    private fun setupListeners() {
        btnBack?.setOnClickListener { finish() }

        btnFotoCek.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
            }
        }

        btnGaleri.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Resim Seç"), GALLERY_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            var resim: Bitmap? = null

            if (requestCode == CAMERA_REQUEST) {
                resim = data?.extras?.get("data") as? Bitmap
            } else if (requestCode == GALLERY_REQUEST) {
                val uri = data?.data
                if (uri != null) {
                    try {
                        resim = if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(this.contentResolver, uri)
                            ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)
                        } else {
                            MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "Resim yüklenemedi", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            resim?.let {
                imgHasar.setImageBitmap(it)
                baslatTaramaEfekti(it)
            }
        }
    }

    private fun baslatTaramaEfekti(resim: Bitmap) {
        cardResult.visibility = View.GONE
        viewScanLine.visibility = View.VISIBLE
        tvStatus.text = "Görüntü İşleniyor..."
        tvStatus.setTextColor(getColor(R.color.primary_blue))
        
        updateTimeline(step = 2) // Move to Analyis

        btnFotoCek.isEnabled = false
        btnGaleri.isEnabled = false

        val height = cardImageContainer.height.toFloat()
        val scanner = ObjectAnimator.ofFloat(viewScanLine, "translationY", 0f, height)
        scanner.duration = 1500
        scanner.repeatCount = 1
        scanner.repeatMode = ValueAnimator.REVERSE
        scanner.interpolator = AccelerateDecelerateInterpolator()
        scanner.start()

        Handler(Looper.getMainLooper()).postDelayed({
            scanner.cancel()
            viewScanLine.visibility = View.GONE
            viewScanLine.translationY = 0f

            tahminEt(resim)

            btnFotoCek.isEnabled = true
            btnGaleri.isEnabled = true
        }, 3000)
    }

    private fun updateTimeline(step: Int) {
        val colorActive = getColor(R.color.primary_blue)
        val colorInactive = getColor(R.color.text_secondary)

        when(step) {
            1 -> {
                tvStep1.setTextColor(colorActive); tvStep1.text = "1. Fotoğraf • MEVCUT"
                tvStep2.setTextColor(colorInactive); tvStep2.text = "2. AI Analizi • Beklemede"
                tvStep3.setTextColor(colorInactive); tvStep3.text = "3. Sonuç • Beklemede"
            }
            2 -> {
                tvStep1.setTextColor(colorActive); tvStep1.text = "1. Fotoğraf • TAMAMLANDI"
                tvStep2.setTextColor(colorActive); tvStep2.text = "2. AI Analizi • İŞLENİYOR..."
                tvStep3.setTextColor(colorInactive); tvStep3.text = "3. Sonuç • Beklemede"
            }
            3 -> {
                tvStep1.setTextColor(colorActive); tvStep1.text = "1. Fotoğraf • TAMAMLANDI"
                tvStep2.setTextColor(colorActive); tvStep2.text = "2. AI Analizi • TAMAMLANDI"
                tvStep3.setTextColor(colorActive); tvStep3.text = "3. Sonuç • HAZIR"
            }
        }
    }

    fun tahminEt(bitmap: Bitmap) {
        try {
            val tfliteModel = FileUtil.loadMappedFile(this, MODEL_NAME)
            val interpreter = Interpreter(tfliteModel)

            val imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(IMAGE_SIZE, IMAGE_SIZE, ResizeOp.ResizeMethod.BILINEAR))
                .add(NormalizeOp(0.0f, 255.0f))
                .build()

            var tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)
            tensorImage = imageProcessor.process(tensorImage)

            val outputBuffer = ByteBuffer.allocateDirect(4 * 3)
            outputBuffer.order(ByteOrder.nativeOrder())

            interpreter.run(tensorImage.buffer, outputBuffer)

            outputBuffer.rewind()
            val hasarPuani = outputBuffer.float
            val saglamPuani = outputBuffer.float
            val gecersizPuani = outputBuffer.float

            Log.d("AI_SONUC", "Hasarlı:$hasarPuani Sağlam:$saglamPuani Geçersiz:$gecersizPuani")

            cardResult.visibility = View.VISIBLE
            cardResult.alpha = 0f
            cardResult.animate().alpha(1f).duration = 500
            
            updateTimeline(3) // Result ready

            if (gecersizPuani > hasarPuani && gecersizPuani > saglamPuani) {
                tvStatus.text = "Tanımlanamadı"
                tvStatus.setTextColor(getColor(android.R.color.darker_gray))
                
                txtSonuc.text = "🚫 ARAÇ BULUNAMADI"
                txtSonuc.setTextColor(getColor(android.R.color.holo_red_dark))
                
                txtDescription.text = "Görüntüde net bir otomobil tespit edemedik. Lütfen tekrar deneyin."
                
                veritabaniKayit("Geçersiz", "Araç Bulunamadı")
            }
            else if (hasarPuani > saglamPuani) {
                tvStatus.text = "Analiz Bitti: Hasarlı"
                tvStatus.setTextColor(getColor(android.R.color.holo_red_light))
                
                txtSonuc.text = "⚠️ HASAR TESPİT EDİLDİ"
                txtSonuc.setTextColor(getColor(android.R.color.holo_orange_light))
                
                txtDescription.text = "Yapay zeka araç yüzeyinde potansiyel hasar izleri tespit etti."
                
                veritabaniKayit("Hasarlı", "Hasar Tespit Edildi")
            }
            else {
                tvStatus.text = "Analiz Bitti: Temiz"
                tvStatus.setTextColor(getColor(R.color.success_green))
                
                txtSonuc.text = "✅ DURUM: TEMİZ"
                txtSonuc.setTextColor(getColor(R.color.success_green))
                
                txtDescription.text = "Aracın görünür yüzeyinde herhangi bir hasar tespit edilmedi."
                
                veritabaniKayit("Sağlam", "Araç Temiz")
            }
            interpreter.close()
            
            findViewById<View>(R.id.controlsArea).visibility = View.GONE
            findViewById<View>(R.id.timelineMethods).visibility = View.GONE
            findViewById<View>(R.id.stepIndicators).visibility = View.GONE
            findViewById<View>(R.id.tvInstructionTitle).visibility = View.GONE
            findViewById<View>(R.id.tvInstructionSub).visibility = View.GONE
            findViewById<View>(R.id.header).visibility = View.GONE
            cardImageContainer.visibility = View.GONE
            
        } catch (e: Exception) {
            tvStatus.text = "Hata Oluştu"
            txtDescription.text = "AI Modeli çalıştırılamadı: ${e.message}"
            cardResult.visibility = View.VISIBLE
            
            Toast.makeText(this, "AI Hatası: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun onResultClose(view: View) {
        cardResult.visibility = View.GONE
        findViewById<View>(R.id.controlsArea).visibility = View.VISIBLE
        findViewById<View>(R.id.timelineMethods).visibility = View.VISIBLE
        findViewById<View>(R.id.stepIndicators).visibility = View.VISIBLE
        findViewById<View>(R.id.tvInstructionTitle).visibility = View.VISIBLE
        findViewById<View>(R.id.tvInstructionSub).visibility = View.VISIBLE
        findViewById<View>(R.id.header).visibility = View.VISIBLE
        cardImageContainer.visibility = View.VISIBLE
        
        imgHasar.setImageResource(R.drawable.ic_launcher_background)
        imgHasar.alpha = 0.6f
        updateTimeline(1)
    }

    fun veritabaniKayit(sonuc: String, detay: String) {
        runOnUiThread {
            try {
                val db = VeritabaniYardimcisi(this)
                db.kayitEkle(sonuc)
                // Toast.makeText(this, "Sonuç kaydedildi", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("DB_HATA", "Kayıt başarısız: ${e.message}")
            }
        }
    }
}
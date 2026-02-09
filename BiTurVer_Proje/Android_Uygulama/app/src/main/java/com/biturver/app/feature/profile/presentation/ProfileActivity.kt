package com.biturver.app.feature.profile.presentation

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.biturver.app.R
import com.biturver.app.feature.auth.presentation.LoginActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileOutputStream

class ProfileActivity : AppCompatActivity() {

    private val prefs by lazy { getSharedPreferences("user_profile", MODE_PRIVATE) }
    private lateinit var imgProfile: ImageView

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            saveImageToInternalStorage(uri)
            imgProfile.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val tb = findViewById<MaterialToolbar>(R.id.toolbarProfile)
        tb.setNavigationOnClickListener { finish() }

        val etFirst = findViewById<TextInputEditText>(R.id.etFirstName)
        val etLast = findViewById<TextInputEditText>(R.id.etLastName)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        // val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val actCity = findViewById<MaterialAutoCompleteTextView>(R.id.actCity)
        val actPayment = findViewById<MaterialAutoCompleteTextView>(R.id.actPayment)
        imgProfile = findViewById(R.id.imgProfile)

        val cities = resources.getStringArray(R.array.turkey_cities).toList()
        val payments = resources.getStringArray(R.array.payment_methods).toList()
        actCity.setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_1, cities))
        actPayment.setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_1, payments))

        loadUserData(etFirst, etLast, etEmail, actCity, actPayment, cities, payments)

        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.btnPickPhoto).setOnClickListener {
            pickImage.launch("image/*")
        }

        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.btnDeletePhoto).setOnClickListener {
            val file = File(filesDir, "profile_pic.jpg")
            if (file.exists()) {
                if (file.delete()) {
                    prefs.edit().putBoolean("has_custom_image", false).apply()
                    imgProfile.setImageResource(android.R.drawable.sym_def_app_icon)
                    Toast.makeText(this, "Profil resmi kaldırıldı 🗑️", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Zaten profil resmi yok", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<MaterialButton>(R.id.btnSaveProfile).setOnClickListener {
            val first = etFirst.text?.toString()?.trim().orEmpty()
            val last = etLast.text?.toString()?.trim().orEmpty()
            val email = etEmail.text?.toString()?.trim().orEmpty()
            val city = actCity.text?.toString()?.trim().orEmpty()
            val payment = actPayment.text?.toString()?.trim().orEmpty()

            if (first.length < 2 || last.length < 2 || city.isBlank() || payment.isBlank()) {
                Toast.makeText(this, "Lütfen eksik alanları doldurun", Toast.LENGTH_SHORT).show()
            } else {
                val gender = "male" // Default or ignored
                prefs.edit()
                    .putString("first_name", first)
                    .putString("last_name", last)
                    .putString("email", email)
                    .putString("gender", gender)
                    .putString("city", city)
                    .putString("payment", payment)
                    .putString("full_name", "$first $last")
                    .apply()
                Toast.makeText(this, "Profil güncellendi ✅", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        findViewById<MaterialButton>(R.id.btnNewAccount).setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Çıkış Yap / Sıfırla")
            builder.setMessage("Mevcut hesaptan çıkış yapıp yeni hesap ekranına dönmek istediğinize emin misiniz?")
            builder.setNegativeButton("Vazgeç") { dialog, _ -> dialog.dismiss() }
            builder.setPositiveButton("Evet, Yap") { _, _ -> performLogout() }
            builder.create().show()
        }


    }

    private fun saveImageToInternalStorage(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(filesDir, "profile_pic.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            prefs.edit().putBoolean("has_custom_image", true).apply()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Resim kaydedilemedi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserData(
        etFirst: TextInputEditText, etLast: TextInputEditText, etEmail: TextInputEditText,
         actCity: MaterialAutoCompleteTextView, actPayment: MaterialAutoCompleteTextView,
        cities: List<String>, payments: List<String>
    ) {
        val fullName = prefs.getString("full_name", "").orEmpty()
        var first = prefs.getString("first_name", "").orEmpty()
        var last = prefs.getString("last_name", "").orEmpty()

        if (first.isBlank() && last.isBlank() && fullName.isNotBlank()) {
            val parts = fullName.trim().split(" ")
            first = parts.firstOrNull().orEmpty()
            last = parts.drop(1).joinToString(" ")
        }

        etFirst.setText(first)
        etLast.setText(last)
        etEmail.isEnabled = true
        etEmail.setText(prefs.getString("email", ""))

        val savedCity = prefs.getString("city", "").orEmpty()
        val savedPayment = prefs.getString("payment", "").orEmpty()

        actCity.setText(cities.find { it.equals(savedCity, true) } ?: savedCity, false)
        actPayment.setText(payments.find { it.equals(savedPayment, true) } ?: savedPayment, false)

        loadProfileImage()
    }

    private fun loadProfileImage() {
        val hasImage = prefs.getBoolean("has_custom_image", false)
        if (hasImage) {
            val file = File(filesDir, "profile_pic.jpg")
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                imgProfile.setImageBitmap(bitmap)
            }
        }
    }

    private fun performLogout() {
        getSharedPreferences("auth", MODE_PRIVATE).edit().clear().apply()
        getSharedPreferences("user_profile", MODE_PRIVATE).edit().clear().apply()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
package com.biturver.app.feature.auth.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.biturver.app.core.ui.MainActivity
import com.biturver.app.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val tilName = findViewById<TextInputLayout>(R.id.tilFullName)
        val tilEmail = findViewById<TextInputLayout>(R.id.tilEmail)
        val tilPass = findViewById<TextInputLayout>(R.id.tilPassword)
        val tilPass2 = findViewById<TextInputLayout>(R.id.tilPassword2)

        val etName = findViewById<TextInputEditText>(R.id.etFullName)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPass = findViewById<TextInputEditText>(R.id.etPassword)
        val etPass2 = findViewById<TextInputEditText>(R.id.etPassword2)

        val tgGender = findViewById<MaterialButtonToggleGroup>(R.id.tgGender)

        findViewById<MaterialButton>(R.id.btnRegister).setOnClickListener {
            tilName.error = null
            tilEmail.error = null
            tilPass.error = null
            tilPass2.error = null

            val fullName = etName.text?.toString()?.trim().orEmpty()
            val email = etEmail.text?.toString()?.trim().orEmpty()
            val pass = etPass.text?.toString().orEmpty()
            val pass2 = etPass2.text?.toString().orEmpty()

            val gender = when (tgGender.checkedButtonId) {
                R.id.btnFemale -> "female"
                else -> "male"
            }

            var ok = true
            if (fullName.length < 3) {
                tilName.error = "Ad soyad gir"
                ok = false
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tilEmail.error = "Geçerli bir e-posta gir"
                ok = false
            }
            if (pass.length < 8) {
                tilPass.error = "Şifre en az 8 karakter olmalı"
                ok = false
            }
            if (pass != pass2) {
                tilPass2.error = "Şifreler aynı değil"
                ok = false
            }

            if (ok) {
                val prefs = getSharedPreferences("user_profile", MODE_PRIVATE)
                prefs.edit()
                    .putString("full_name", fullName)
                    .putString("gender", gender) // male / female
                    .putString("email", email)
                    .apply()

                val i = Intent(this, MainActivity::class.java)
                i.data = intent?.data
                startActivity(i)
                finish()
            }
            if (ok) {

                getSharedPreferences("auth", MODE_PRIVATE)
                    .edit()
                    .putBoolean("logged_in", true)
                    .apply()

                val i = Intent(this, MainActivity::class.java)
                i.data = intent?.data
                startActivity(i)
                finish()
            }

        }

        findViewById<MaterialButton>(R.id.btnGoLogin).setOnClickListener {
            finish()
        }
    }
}
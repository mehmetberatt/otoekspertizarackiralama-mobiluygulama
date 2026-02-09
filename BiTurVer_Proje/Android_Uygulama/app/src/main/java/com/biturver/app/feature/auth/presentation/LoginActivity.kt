package com.biturver.app.feature.auth.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.biturver.app.core.ui.MainActivity
import com.biturver.app.R
import com.biturver.app.feature.auth.presentation.RegisterActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tilEmail = findViewById<TextInputLayout>(R.id.tilEmail)
        val tilPass = findViewById<TextInputLayout>(R.id.tilPassword)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPass = findViewById<TextInputEditText>(R.id.etPassword)

        findViewById<MaterialButton>(R.id.btnLogin).setOnClickListener {
            tilEmail.error = null
            tilPass.error = null

            val email = etEmail.text?.toString()?.trim().orEmpty()
            val pass = etPass.text?.toString().orEmpty()

            var ok = true
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tilEmail.error = "Geçerli bir e-posta gir"
                ok = false
            }
            if (pass.length < 8) {
                tilPass.error = "Şifre en az 8 karakter olmalı"
                ok = false
            }

            if (ok) {
                val i = Intent(this, MainActivity::class.java)
                i.data = intent?.data // deep link geldiyse taşır
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

        findViewById<MaterialButton>(R.id.btnGoRegister).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java).apply {
                data = intent?.data
            })
        }
    }
}
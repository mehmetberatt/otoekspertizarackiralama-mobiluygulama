package com.biturver.app.feature.profile.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.biturver.app.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarContact)
        toolbar.setNavigationOnClickListener { finish() }

        val etSubject = findViewById<TextInputEditText>(R.id.etSubject)
        val etMessage = findViewById<TextInputEditText>(R.id.etMessage)
        val btnSend = findViewById<MaterialButton>(R.id.btnSendMessage)

        btnSend.setOnClickListener {
            val subject = etSubject.text.toString()
            val message = etMessage.text.toString()

            if (subject.isBlank() || message.isBlank()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Mesajınız iletildi! En kısa sürede döneceğiz. ✅", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}
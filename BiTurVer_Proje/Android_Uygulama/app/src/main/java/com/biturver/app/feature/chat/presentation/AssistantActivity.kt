package com.biturver.app.feature.chat.presentation

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.biturver.app.R

class AssistantActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assistant)

        findViewById<View>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }
        
        val etMessage = findViewById<EditText>(R.id.etMessage)
        
        // Suggestion Buttons
        findViewById<View>(R.id.btnSuggestRent).setOnClickListener {
            etMessage.setText("Araç kiralamak istiyorum")
        }
        
        findViewById<View>(R.id.btnSuggestExpert).setOnClickListener {
            etMessage.setText("Ekspertiz durumu hakkında bilgi alabilir miyim?")
        }
        
        findViewById<View>(R.id.btnSuggestDamage).setOnClickListener {
            etMessage.setText("Aracımda hasar tespiti yapmak istiyorum")
        }
        
        // Send Button
        findViewById<View>(R.id.btnSend).setOnClickListener {
            val msg = etMessage.text.toString()
            if (msg.isNotBlank()) {
                // Simulate sending
                etMessage.text.clear()
                Toast.makeText(this, "Mesaj gönderildi: $msg", Toast.LENGTH_SHORT).show()
                // Here you would typically add to RecyclerView adapter
            }
        }
    }
}
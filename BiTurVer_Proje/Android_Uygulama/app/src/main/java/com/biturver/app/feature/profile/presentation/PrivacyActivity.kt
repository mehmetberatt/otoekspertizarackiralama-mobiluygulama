package com.biturver.app.feature.profile.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.biturver.app.R
import com.google.android.material.appbar.MaterialToolbar

class PrivacyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarPrivacy)
        toolbar.setNavigationOnClickListener { finish() }
    }
}
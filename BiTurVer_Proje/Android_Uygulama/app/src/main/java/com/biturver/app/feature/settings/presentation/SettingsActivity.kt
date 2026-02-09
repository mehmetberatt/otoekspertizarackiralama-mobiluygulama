package com.biturver.app.feature.settings.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.biturver.app.R
import com.biturver.app.feature.profile.presentation.ContactActivity
import com.biturver.app.feature.profile.presentation.PrivacyActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    companion object {
        private const val PREFS = "app_settings"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_NOTIFICATIONS = "notifications"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        applySavedTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)


        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarSettings)
        toolbar.setNavigationOnClickListener { finish() }


        val swNotify = findViewById<SwitchMaterial>(R.id.swNotifications)
        swNotify.isChecked = prefs.getBoolean(KEY_NOTIFICATIONS, true)

        swNotify.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(KEY_NOTIFICATIONS, isChecked).apply()
            val msg = if (isChecked) "Bildirimler açıldı 🔔" else "Bildirimler kapatıldı 🔕"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        setupThemeSelection()

        val rgLang = findViewById<RadioGroup>(R.id.rgLanguage)
        rgLang.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbEN) {
                Toast.makeText(this, "İngilizce seçeneği çok yakında! 🇬🇧", Toast.LENGTH_SHORT).show()
                group.post { group.check(R.id.rbTR) }
            }
        }

        findViewById<TextView>(R.id.btnPrivacy).setOnClickListener {

            startActivity(Intent(this, PrivacyActivity::class.java))
        }

        findViewById<TextView>(R.id.btnContact).setOnClickListener {

            startActivity(Intent(this, ContactActivity::class.java))
        }
    }

    private fun setupThemeSelection() {
        val rgTheme = findViewById<RadioGroup>(R.id.rgTheme)
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)


        val savedMode = prefs.getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        rgTheme.setOnCheckedChangeListener(null)
        when (savedMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> rgTheme.check(R.id.rbLight)
            AppCompatDelegate.MODE_NIGHT_YES -> rgTheme.check(R.id.rbDark)
            else -> rgTheme.check(R.id.rbSystem)
        }


        rgTheme.setOnCheckedChangeListener { _, checkedId ->
            val newMode = when (checkedId) {
                R.id.rbLight -> AppCompatDelegate.MODE_NIGHT_NO
                R.id.rbDark -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }

            if (newMode != savedMode) {
                prefs.edit().putInt(KEY_THEME_MODE, newMode).apply()
                AppCompatDelegate.setDefaultNightMode(newMode)


                recreate()
            }
        }
    }

    private fun applySavedTheme() {
        val prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        val mode = prefs.getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
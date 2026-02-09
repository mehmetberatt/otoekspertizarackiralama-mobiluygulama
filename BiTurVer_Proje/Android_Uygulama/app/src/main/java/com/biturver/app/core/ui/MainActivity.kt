package com.biturver.app.core.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.biturver.app.R
import com.biturver.app.feature.auth.presentation.LoginActivity
import com.biturver.app.feature.car.presentation.RentCarActivity
import com.biturver.app.feature.chat.presentation.AssistantActivity
import com.biturver.app.feature.damage.presentation.DamageDetectionActivity
import com.biturver.app.feature.license.presentation.LicenseScanActivity
import com.biturver.app.feature.profile.presentation.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupHeader()
        setupQuickActions()
        setupBottomNavigation()
        setupBottomNavigation()
        setupFeaturedCars()
    }

    private fun setupHeader() {
        val tvUsername = findViewById<TextView>(R.id.tvUsername)
        val ivAvatar = findViewById<ShapeableImageView>(R.id.ivAvatar)

        // Load user info from SharedPreferences
        val prefs = getSharedPreferences("user_profile", MODE_PRIVATE)
        val fullName = prefs.getString("full_name", "")
        tvUsername.text = if (!fullName.isNullOrBlank()) fullName else "Hoş Geldiniz"

        // Load profile picture
        val file = File(filesDir, "profile_pic.jpg")
        if (file.exists()) {
            try {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                ivAvatar.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        ivAvatar.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
        // Custom Bottom Navigation Logic
        
        findViewById<View>(R.id.nav_home).setOnClickListener {
            // Already on home, maybe scroll to top?
        }

        findViewById<View>(R.id.nav_rent).setOnClickListener {
            startActivity(Intent(this, RentCarActivity::class.java))
        }

        findViewById<View>(R.id.nav_ai).setOnClickListener {
            startActivity(Intent(this, AssistantActivity::class.java))
        }
        
        findViewById<View>(R.id.nav_apps).setOnClickListener {
             startActivity(Intent(this, com.biturver.app.feature.history.presentation.HistoryActivity::class.java))
        }

        findViewById<View>(R.id.nav_profile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        
        // Settings Button logic (Top Right)
        findViewById<View>(R.id.btnMainSettings).setOnClickListener {
             startActivity(Intent(this, com.biturver.app.feature.settings.presentation.SettingsActivity::class.java))
        }
    }
    
    // Quick Actions setup updated to match new IDs
    private fun setupQuickActions() {
        // Helper to setup action button
        fun setupAction(includeId: Int, title: String? = null, iconRes: Int? = null, onClick: () -> Unit) {
            val view = findViewById<View>(includeId)
            
            if (title != null) {
                val tvTitle = view.findViewById<TextView>(R.id.tvActionTitle)
                if (tvTitle != null) tvTitle.text = title
            }
            
            if (iconRes != null) {
                val ivIcon = view.findViewById<ImageView>(R.id.ivActionIcon)
                 if (ivIcon != null) ivIcon.setImageResource(iconRes)
            }

            view.setOnClickListener { onClick() }
        }

        setupAction(R.id.actionRent, "Araç Kirala", android.R.drawable.ic_menu_compass) {
            startActivity(Intent(this, RentCarActivity::class.java))
        }
        
        setupAction(R.id.actionDamage, "Hasar Tahmini\n& Ekspertiz", android.R.drawable.ic_menu_camera) {
             startActivity(Intent(this, DamageDetectionActivity::class.java))
        }
        
        setupAction(R.id.actionLicense, "Ehliyet\nDoğrulama", android.R.drawable.ic_menu_myplaces) {
             startActivity(Intent(this, LicenseScanActivity::class.java))
        }
        
        setupAction(R.id.actionAppointments, "Randevularım", android.R.drawable.ic_menu_today) {
             startActivity(Intent(this, com.biturver.app.feature.history.presentation.HistoryActivity::class.java))
        }

    }

    private fun setupFeaturedCars() {
         val rv = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvFeaturedCars)
         rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
         
         // Dummy Data
         val dummyCars = listOf(
            com.biturver.app.feature.car.model.Car(
                brand = "Renault",
                model = "Clio",
                type = "Ekonomi",
                transmission = "Otomatik",
                fuel = "Benzin",
                seats = 5,
                city = "İstanbul",
                pricePerDay = 1200,
                color = com.biturver.app.feature.car.model.CarColor.WHITE,
                rating = 4.8,
                reviewCount = 124,
                imageRes = R.drawable.car_renault_clio
            ),
            com.biturver.app.feature.car.model.Car(
                brand = "Fiat",
                model = "Egea",
                type = "Konfor",
                transmission = "Manuel",
                fuel = "Dizel",
                seats = 5,
                city = "Ankara",
                pricePerDay = 1450,
                color = com.biturver.app.feature.car.model.CarColor.GREY,
                rating = 4.6,
                reviewCount = 89,
                imageRes = R.drawable.car_fiat_egea
            ),
             com.biturver.app.feature.car.model.Car(
                brand = "Peugeot",
                model = "3008",
                type = "SUV",
                transmission = "Otomatik",
                fuel = "Dizel",
                seats = 5,
                city = "İzmir",
                pricePerDay = 2800,
                color = com.biturver.app.feature.car.model.CarColor.BLACK,
                rating = 4.9,
                reviewCount = 45,
                imageRes = R.drawable.car_peugeot_3008
            )
         )

         val adapter = com.biturver.app.feature.car.adapter.FeaturedCarAdapter(dummyCars) { car ->
             // On car click
             startActivity(Intent(this, RentCarActivity::class.java))
         }
         rv.adapter = adapter
    }



    override fun onResume() {
        super.onResume()
        setupHeader()
    }
}
package com.biturver.app.feature.car.presentation

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.biturver.app.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class CarDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarDetail)
        toolbar.setNavigationOnClickListener { finish() }

        val model = intent.getStringExtra("car_model") ?: "Araç"
        val price = intent.getIntExtra("car_price", 0)
        val imageRes = intent.getIntExtra("car_image", 0)
        val location = intent.getStringExtra("car_location") ?: "-"
        val fuel = intent.getStringExtra("car_fuel") ?: "-"
        val gear = intent.getStringExtra("car_gear") ?: "-"

        val imgCar = findViewById<ImageView>(R.id.imgDetailCar)
        val tvTitle = findViewById<TextView>(R.id.tvDetailTitle)
        val tvPrice = findViewById<TextView>(R.id.tvDetailPrice)
        val tvLocation = findViewById<TextView>(R.id.tvDetailLocation)
        val tvFuel = findViewById<TextView>(R.id.tvDetailFuel)
        val tvGear = findViewById<TextView>(R.id.tvDetailGear)
        val btnRent = findViewById<MaterialButton>(R.id.btnRentNow)

        tvTitle.text = model
        tvPrice.text = "₺$price / gün"
        tvLocation.text = location
        tvFuel.text = fuel
        tvGear.text = gear

        if (imageRes != 0) {
            imgCar.setImageResource(imageRes)
        }


        val startDate = intent.getLongExtra("start_date", 0L)
        val endDate = intent.getLongExtra("end_date", 0L)

        if (startDate != 0L) {
             val sdfDebug = java.text.SimpleDateFormat("dd MMM HH:mm", java.util.Locale("tr"))
             Toast.makeText(this, "Seçilen Tarih: ${sdfDebug.format(java.util.Date(startDate))}", Toast.LENGTH_SHORT).show()
        }

        val dateString = if (startDate != 0L && endDate != 0L) {
            val sdf = java.text.SimpleDateFormat("dd MMM", java.util.Locale("tr"))
            "${sdf.format(java.util.Date(startDate))} - ${sdf.format(java.util.Date(endDate))}"
        } else {
             "1 Günlük (Standart)"
        }

        btnRent.setOnClickListener {
            val pickUpTime = if (startDate != 0L) startDate else System.currentTimeMillis() + (48 * 60 * 60 * 1000)

            val record = com.biturver.app.data.local.ReservationRecord(
                id = com.biturver.app.data.local.ReservationStore.newId(),
                title = "$model",
                meta = "$fuel, $gear",
                dates = dateString,
                totalPrice = price,
                pickUpMillis = pickUpTime,
                status = "Onay Bekliyor"
            )
            com.biturver.app.data.local.ReservationStore.add(this, record)

            Toast.makeText(this, "Rezervasyon alındı! ✅\nRandevularım sekmesinde.", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
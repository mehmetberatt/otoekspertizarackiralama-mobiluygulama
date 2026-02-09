package com.biturver.app.feature.car.model

import com.biturver.app.feature.car.model.CarColor
import java.io.Serializable

data class Car(
    val brand: String,
    val model: String,
    val type: String,        // Örn: SUV, Sedan
    val transmission: String,// Örn: Otomatik, Manuel
    val fuel: String,        // Örn: Benzin, Dizel
    val seats: Int,
    val city: String,
    val pricePerDay: Int,
    val color: CarColor,
    val imageRes: Int,       // Resim ID'si
    val rating: Double = 4.6,
    val reviewCount: Int = 120,
    val luggageLiters: Int = 380,
    val doors: Int = 5
) : Serializable
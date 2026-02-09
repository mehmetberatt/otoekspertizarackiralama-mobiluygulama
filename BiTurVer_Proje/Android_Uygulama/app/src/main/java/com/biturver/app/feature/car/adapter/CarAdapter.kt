package com.biturver.app.feature.car.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.biturver.app.feature.car.model.Car
import com.biturver.app.R
import com.google.android.material.button.MaterialButton

class CarAdapter(
    private var cars: List<Car>,
    private val onCarClick: (Car) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {


    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newCars: List<Car>) {
        cars = newCars
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(cars[position])
    }

    override fun getItemCount(): Int = cars.size

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgCar: ImageView = itemView.findViewById(R.id.imgCar)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvCarTitle)
        private val tvType: TextView = itemView.findViewById(R.id.tvCarType)
        private val tvRating: TextView = itemView.findViewById(R.id.tvCarRating)
        private val tvTransmission: TextView = itemView.findViewById(R.id.tvTransmission)
        private val tvFuel: TextView = itemView.findViewById(R.id.tvFuel)
        private val tvSeats: TextView = itemView.findViewById(R.id.tvSeats)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvCarPrice)
        private val btnSelect: MaterialButton = itemView.findViewById(R.id.btnSelect)

        @SuppressLint("SetTextI18n")
        fun bind(car: Car) {
            tvTitle.text = "${car.brand} ${car.model}" 
            tvType.text = car.type
            tvRating.text = "${car.rating} (${car.reviewCount})"
            tvTransmission.text = car.transmission
            tvFuel.text = car.fuel
            tvSeats.text = "${car.seats} Kişi"
            tvPrice.text = "₺${car.pricePerDay}"

            if (car.imageRes != 0) {
                imgCar.setImageResource(car.imageRes)
            }
            
            // Set click listeners
            btnSelect.setOnClickListener { onCarClick(car) }
            itemView.setOnClickListener { onCarClick(car) }
        }
    }
}
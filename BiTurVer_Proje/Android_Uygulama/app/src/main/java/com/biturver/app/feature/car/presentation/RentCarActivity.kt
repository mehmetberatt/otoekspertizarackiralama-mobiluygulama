package com.biturver.app.feature.car.presentation

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biturver.app.feature.car.model.Car
import com.biturver.app.feature.car.adapter.CarAdapter
import com.biturver.app.feature.car.model.CarColor
import com.biturver.app.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RentCarActivity : AppCompatActivity() {

    private lateinit var carAdapter: CarAdapter

    private lateinit var spinnerCity: Spinner
    private lateinit var etSearch: EditText
    private lateinit var cardDateFilter: View
    private lateinit var tvDateRange: TextView
    private lateinit var tvResultsCount: TextView
    private lateinit var tvEmptyCars: TextView
    private lateinit var rvCars: RecyclerView
    private lateinit var toolbar: MaterialToolbar


    private var allCars = listOf<Car>()
    private var filteredCars = mutableListOf<Car>()


    private var currentCity: String = "Tümü"
    private var currentSearchText: String = ""
    private var selectedStartDate: Long = 0L
    private var selectedEndDate: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent_car)


        toolbar = findViewById(R.id.toolbarRent)
        spinnerCity = findViewById(R.id.spinnerCity)
        etSearch = findViewById(R.id.etSearch)
        cardDateFilter = findViewById(R.id.cardDateFilter)
        tvDateRange = findViewById(R.id.tvDateRange)
        tvResultsCount = findViewById(R.id.tvResultsCount)
        tvEmptyCars = findViewById(R.id.tvEmptyCars)
        rvCars = findViewById(R.id.rvCars)

        toolbar.setNavigationOnClickListener { finish() }


        val cities = listOf("Tümü", "İstanbul", "Ankara", "İzmir", "Antalya", "Bursa", "Adana", "Trabzon", "Muğla", "Konya", "Gaziantep")
        val cityAdapter = ArrayAdapter(this, R.layout.item_spinner, cities)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity.adapter = cityAdapter
        
        spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                currentCity = cities[position]
                applyFilters()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        allCars = sampleCars()
        filteredCars.addAll(allCars)


        carAdapter = CarAdapter(filteredCars) { car ->
            val intent = Intent(this, CarDetailActivity::class.java)
            intent.putExtra("car_model", "${car.brand} ${car.model}")
            intent.putExtra("car_price", car.pricePerDay)
            intent.putExtra("car_image", car.imageRes)
            intent.putExtra("car_location", car.city)
            intent.putExtra("car_fuel", car.fuel)
            intent.putExtra("car_gear", car.transmission)
             intent.putExtra("start_date", selectedStartDate)
            intent.putExtra("end_date", selectedEndDate)
            startActivity(intent)
        }
        rvCars.layoutManager = LinearLayoutManager(this)
        rvCars.adapter = carAdapter
        updateCountText()


        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                currentSearchText = s.toString()
                applyFilters()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        cardDateFilter.setOnClickListener {
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Kiralama Tarihleri")
                .setSelection(
                    Pair(
                        MaterialDatePicker.todayInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

            picker.addOnPositiveButtonClickListener { selection ->
                selectedStartDate = selection.first
                selectedEndDate = selection.second
                
                val sdf = SimpleDateFormat("dd MMM", Locale("tr"))
                val startStr = sdf.format(Date(selection.first))
                val endStr = sdf.format(Date(selection.second))
                tvDateRange.text = "$startStr - $endStr"
            }
            picker.show(supportFragmentManager, "DATE_PICKER")
        }
    }

    private fun applyFilters() {
        filteredCars.clear()
        val searchLower = currentSearchText.lowercase(Locale("tr"))

        for (car in allCars) {
            val cityMatch = if (currentCity == "Tümü") true else car.city.equals(currentCity, ignoreCase = true)

            val searchMatch = if (searchLower.isBlank()) true else {
                car.brand.lowercase(Locale("tr")).contains(searchLower) ||
                        car.model.lowercase(Locale("tr")).contains(searchLower)
            }

            if (cityMatch && searchMatch) {
                filteredCars.add(car)
            }
        }

        carAdapter.updateList(filteredCars)
        updateCountText()

        if (filteredCars.isEmpty()) {
            rvCars.visibility = View.GONE
            tvEmptyCars.visibility = View.VISIBLE
        } else {
            rvCars.visibility = View.VISIBLE
            tvEmptyCars.visibility = View.GONE
        }
    }

    private fun updateCountText() {
        tvResultsCount.text = "${filteredCars.size} araç listeleniyor"
    }

    private fun sampleCars(): List<Car> = listOf(

        Car(
            "Renault",
            "Clio",
            "Hatchback",
            "Otomatik",
            "Benzin",
            5,
            "İstanbul",
            1450,
            CarColor.BLUE,
            R.drawable.car_renault_clio
        ),
        Car(
            "Fiat",
            "Egea",
            "Sedan",
            "Manuel",
            "Dizel",
            5,
            "İstanbul",
            1500,
            CarColor.WHITE,
            R.drawable.car_fiat_egea
        ),
        Car(
            "Fiat",
            "Egea",
            "Sedan",
            "Otomatik",
            "Dizel",
            5,
            "Ankara",
            1600,
            CarColor.GREY,
            R.drawable.car_fiat_egea
        ),
        Car(
            "Hyundai",
            "i20",
            "Hatchback",
            "Otomatik",
            "Benzin",
            5,
            "İzmir",
            1550,
            CarColor.RED,
            R.drawable.car_hyundai_i20
        ),
        Car(
            "Renault",
            "Taliant",
            "Sedan",
            "Manuel",
            "Benzin",
            5,
            "Antalya",
            1400,
            CarColor.WHITE,
            R.drawable.car_renault_taliant
        ),
        Car(
            "Opel",
            "Corsa",
            "Hatchback",
            "Otomatik",
            "Benzin",
            5,
            "Gaziantep",
            1700,
            CarColor.RED,
            R.drawable.car_opel_corsa
        ),


        Car(
            "Toyota",
            "Corolla",
            "Sedan",
            "Otomatik",
            "Hibrit",
            5,
            "İstanbul",
            2100,
            CarColor.WHITE,
            R.drawable.car_toyota_corolla
        ),
        Car(
            "Ford",
            "Focus",
            "Hatchback",
            "Otomatik",
            "Dizel",
            5,
            "İzmir",
            2200,
            CarColor.BLUE,
            R.drawable.car_ford_focus
        ),
        Car(
            "Honda",
            "Civic",
            "Sedan",
            "Otomatik",
            "Benzin",
            5,
            "Bursa",
            2300,
            CarColor.BLACK,
            R.drawable.car_honda_civic
        ),
        Car(
            "Skoda",
            "Octavia",
            "Sedan",
            "Otomatik",
            "Dizel",
            5,
            "İstanbul",
            2400,
            CarColor.WHITE,
            R.drawable.car_skoda_octavia
        ),
        Car(
            "Renault",
            "Megane",
            "Sedan",
            "Otomatik",
            "Dizel",
            5,
            "Adana",
            2000,
            CarColor.GREY,
            R.drawable.car_renault_megane
        ),
        Car(
            "VW",
            "Passat",
            "Sedan",
            "Otomatik",
            "Dizel",
            5,
            "Konya",
            2800,
            CarColor.BLACK,
            R.drawable.car_vw_passat
        ),


        Car(
            "Peugeot",
            "3008",
            "SUV",
            "Otomatik",
            "Dizel",
            5,
            "İstanbul",
            3200,
            CarColor.ORANGE,
            R.drawable.car_peugeot_3008
        ),
        Car(
            "Nissan",
            "Qashqai",
            "SUV",
            "Otomatik",
            "Benzin",
            5,
            "Antalya",
            3100,
            CarColor.RED,
            R.drawable.car_nissan_qashqai
        ),
        Car(
            "Dacia",
            "Duster",
            "SUV",
            "Manuel",
            "Dizel",
            5,
            "Muğla",
            1900,
            CarColor.GREEN,
            R.drawable.car_dacia_duster
        ),
        Car(
            "Volvo",
            "XC90",
            "Lüks SUV",
            "Otomatik",
            "Hibrit",
            7,
            "İstanbul",
            6500,
            CarColor.BLACK,
            R.drawable.car_volvo_xc90
        ),


        Car(
            "BMW",
            "320i",
            "Lüks",
            "Otomatik",
            "Benzin",
            5,
            "İstanbul",
            4500,
            CarColor.BLUE,
            R.drawable.car_bmw_320i
        ),
        Car(
            "Mercedes",
            "C200",
            "Lüks",
            "Otomatik",
            "Dizel",
            5,
            "Ankara",
            5000,
            CarColor.WHITE,
            R.drawable.car_mercedes_c200
        ),
        Car(
            "Audi",
            "A4",
            "Lüks",
            "Otomatik",
            "Benzin",
            5,
            "İzmir",
            4800,
            CarColor.BLACK,
            R.drawable.car_audi_a4
        ),
        Car(
            "Mercedes",
            "E180",
            "Lüks",
            "Otomatik",
            "Benzin",
            5,
            "Antalya",
            5500,
            CarColor.GREY,
            R.drawable.car_mercedes_e180
        )
    )
}
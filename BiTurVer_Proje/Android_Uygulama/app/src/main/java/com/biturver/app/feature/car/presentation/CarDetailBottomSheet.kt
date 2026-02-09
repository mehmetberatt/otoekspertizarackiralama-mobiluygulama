package com.biturver.app.feature.car.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.biturver.app.feature.car.model.Car
import com.biturver.app.R
import com.biturver.app.data.local.ReservationRecord
import com.biturver.app.data.local.ReservationStore
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CarDetailBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.bottomsheet_car_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = requireArguments()

        val img = view.findViewById<ImageView>(R.id.imgCarHero)
        val tvTitle = view.findViewById<TextView>(R.id.tvHeroTitle)
        val tvMeta = view.findViewById<TextView>(R.id.tvHeroMeta)
        val tvDates = view.findViewById<TextView>(R.id.tvHeroDates)
        val tvPrice = view.findViewById<TextView>(R.id.tvHeroPrice)
        val tvTotal = view.findViewById<TextView>(R.id.tvHeroTotal)
        val btn = view.findViewById<MaterialButton>(R.id.btnReserveDemo)

        // Verileri Argument'lardan çekiyoruz
        val imageRes = args.getInt(ARG_IMAGE)
        val title = args.getString(ARG_TITLE).orEmpty()
        val meta = args.getString(ARG_META).orEmpty()
        val dates = args.getString(ARG_DATES).orEmpty()
        val priceText = args.getString(ARG_PRICE).orEmpty()
        val totalText = args.getString(ARG_TOTAL).orEmpty()
        val totalPriceValue = args.getInt(ARG_TOTAL_VALUE, 0)

        // KRİTİK: Eğer pickUpTime 0 gelirse, 24 saat kuralı bozulmasın diye
        // varsayılan olarak şu ana 2 gün (48 saat) ekliyoruz.
        var pickUpTime = args.getLong(ARG_PICKUP_TIME)
        if (pickUpTime <= 0) {
            pickUpTime = System.currentTimeMillis() + (48 * 60 * 60 * 1000)
        }

        img.setImageResource(imageRes)
        tvTitle.text = title
        tvMeta.text = meta
        tvDates.text = dates
        tvPrice.text = priceText
        tvTotal.text = totalText

        btn.setOnClickListener {
            try {
                // 1. Yeni bir ID oluştur
                val reservationId = ReservationStore.newId()

                // 2. Kayıt nesnesini oluştur (HistoryActivity'de görünen tüm alanlar)
                val newRecord = ReservationRecord(
                    id = reservationId,
                    title = title,
                    meta = meta,
                    dates = dates,
                    totalPrice = totalPriceValue,
                    pickUpMillis = pickUpTime // 24 saat kuralı için bu tarih saklanıyor
                )

                // 3. Veriyi SharedPreferences'a (ReservationStore) KAYDET
                ReservationStore.add(requireContext(), newRecord)

                // 4. Kullanıcıya Onay Ver
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Rezervasyon Onaylandı ✅")
                    .setMessage("Rezervasyon No: $reservationId\nAraç: $title\n\nBu işlemi 'Geçmiş İşlemler' ekranından görebilir ve yönetebilirsiniz.")
                    .setPositiveButton("Harika") { _, _ ->
                        dismiss()
                    }
                    .setCancelable(false)
                    .show()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Hata: Rezervasyon kaydedilemedi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val ARG_IMAGE = "image"
        private const val ARG_TITLE = "title"
        private const val ARG_META = "meta"
        private const val ARG_DATES = "dates"
        private const val ARG_PRICE = "price"
        private const val ARG_TOTAL = "total"
        private const val ARG_TOTAL_VALUE = "total_value"
        private const val ARG_PICKUP_TIME = "pickup_time"

        fun newInstance(
            car: Car,
            dayCount: Int,
            totalPrice: Int,
            pickUpText: String,
            dropOffText: String,
            pickUpMillis: Long
        ): CarDetailBottomSheet {
            val b = Bundle().apply {
                putInt(ARG_IMAGE, car.imageRes)
                putString(ARG_TITLE, "${car.brand} ${car.model}")
                putString(ARG_META, "${car.type} • ${car.transmission} • ${car.fuel} • ${car.city}")
                putString(ARG_DATES, "Alış: $pickUpText  •  Dönüş: $dropOffText  •  $dayCount gün")
                putString(ARG_PRICE, "Günlük: ₺${car.pricePerDay}")
                putString(ARG_TOTAL, "Toplam: ₺$totalPrice")
                putInt(ARG_TOTAL_VALUE, totalPrice)
                putLong(ARG_PICKUP_TIME, pickUpMillis) // Burası 24 saat kontrolü için History'e gidiyor
            }
            return CarDetailBottomSheet().apply { arguments = b }
        }
    }
}
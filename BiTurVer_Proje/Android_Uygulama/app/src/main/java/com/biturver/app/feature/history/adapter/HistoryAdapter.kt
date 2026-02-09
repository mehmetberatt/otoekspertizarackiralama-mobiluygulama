package com.biturver.app.feature.history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.biturver.app.R
import com.biturver.app.data.local.ReservationRecord
import com.google.android.material.button.MaterialButton

class HistoryAdapter(
    private val onCancelClick: (ReservationRecord, Int) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.VH>() {

    val items = mutableListOf<ReservationRecord>()

    fun submit(newItems: List<ReservationRecord>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun removeItemAt(position: Int): ReservationRecord {
        val removed = items.removeAt(position)
        notifyItemRemoved(position)
        return removed
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle: TextView = v.findViewById(R.id.tvResTitle)
        val tvDates: TextView = v.findViewById(R.id.tvResDates)
        val tvId: TextView = v.findViewById(R.id.tvResId)
        val tvPrice: TextView = v.findViewById(R.id.tvResPrice)
        val tvStatus: TextView = v.findViewById(R.id.tvResStatus)
        val btnCancel: MaterialButton = v.findViewById(R.id.btnCancelRes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_reservation, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        holder.tvTitle.text = item.title
        holder.tvDates.text = item.dates
        holder.tvId.text = "Ref: ${item.id}"
        holder.tvPrice.text = "₺${item.totalPrice}"


        holder.tvStatus.text = "Durum: Aktif"

        holder.btnCancel.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            val reservationTime = item.pickUpMillis
            val diff = reservationTime - currentTime

            val twentyFourHoursMs = 86400000L

            if (diff < twentyFourHoursMs) {
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle("İptal Edilemez")
                    .setMessage("Rezervasyona 24 saatten az kaldığı için uygulama üzerinden iptal yapılamaz. Lütfen destek birimiyle görüşün.")
                    .setPositiveButton("Anladım", null)
                    .show()
            } else {
                onCancelClick(item, holder.adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
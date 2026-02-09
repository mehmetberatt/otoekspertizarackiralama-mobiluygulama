package com.biturver.app.feature.history.presentation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biturver.app.R
import com.biturver.app.data.local.ReservationRecord
import com.biturver.app.data.local.ReservationStore
import com.biturver.app.feature.history.adapter.HistoryAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class HistoryActivity : AppCompatActivity() {

    private lateinit var adapter: HistoryAdapter
    private lateinit var rv: RecyclerView
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarHistory)
        toolbar.setNavigationOnClickListener { finish() }

        rv = findViewById(R.id.rvHistory)
        tvEmpty = findViewById(R.id.tvEmptyHistory)
        rv.layoutManager = LinearLayoutManager(this)

        adapter = HistoryAdapter { item, position ->
            confirmAndDelete(item, position)
        }
        rv.adapter = adapter

        loadData()
        setupSwipeToDelete()
    }

    private fun confirmAndDelete(item: ReservationRecord, position: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Kaydı Sil")
            .setMessage("Bu geçmiş kaydını silmek istediğinize emin misiniz?")
            .setNegativeButton("Vazgeç") { _, _ -> adapter.notifyItemChanged(position) }
            .setPositiveButton("Evet, Sil") { _, _ ->
                adapter.removeItemAt(position)
                ReservationStore.remove(this, item.id)
                checkEmpty()
                Snackbar.make(rv, "Kayıt silindi", Snackbar.LENGTH_SHORT).show()
            }
            .setOnCancelListener { adapter.notifyItemChanged(position) }
            .show()
    }

    private fun loadData() {
        val data = ReservationStore.getAll(this)
        if (data.isEmpty()) {
            rv.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        } else {
            rv.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
            adapter.submit(data)
        }
    }

    private fun checkEmpty() {
        if (adapter.itemCount == 0) {
            rv.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        }
    }

    private fun setupSwipeToDelete() {
        val deleteBackground = ColorDrawable(Color.parseColor("#E53935"))

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(r: RecyclerView, v: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val item = adapter.items[pos]

                // DÜZELTME: Kaydırma yapıldığında 24 saat kontrolü
                val currentTime = System.currentTimeMillis()
                val diff = item.pickUpMillis - currentTime
                val twentyFourHoursMs = 86400000L

                if (diff < twentyFourHoursMs) {
                    MaterialAlertDialogBuilder(this@HistoryActivity)
                        .setTitle("İşlem Reddedildi")
                        .setMessage("Rezervasyona 24 saatten az kaldığı için silme yapılamaz.")
                        .setPositiveButton("Tamam") { _, _ -> adapter.notifyItemChanged(pos) }
                        .show()
                } else {
                    confirmAndDelete(item, pos)
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv)
    }
}
package com.biturver.app.data.local

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

data class ReservationRecord(
    val id: String,
    val title: String,
    val meta: String,
    val dates: String,
    val totalPrice: Int,
    val pickUpMillis: Long,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val status: String = "Aktif"
)

object ReservationStore {
    private const val PREF = "biturver_reservations"
    private const val KEY = "list"
    private const val MAX = 50

    fun newId(): String {
        val date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
        val rnd = Random.Default.nextInt(1000, 9999)
        return "BV-$date-$rnd"
    }

    fun add(context: Context, record: ReservationRecord) {
        val all = getAll(context).toMutableList()
        all.add(0, record)
        if (all.size > MAX) {
            all.subList(MAX, all.size).clear()
        }
        saveAll(context, all)
    }

    fun remove(context: Context, recordId: String) {
        val all = getAll(context).toMutableList()
        val wasRemoved = all.removeIf { it.id == recordId }
        if (wasRemoved) {
            saveAll(context, all)
        }
    }

    fun getAll(context: Context): List<ReservationRecord> {
        val sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val raw = sp.getString(KEY, null) ?: return emptyList()
        return try {
            val arr = JSONArray(raw)
            buildList {
                for (i in 0 until arr.length()) {
                    val o = arr.getJSONObject(i)
                    add(
                        ReservationRecord(
                            id = o.getString("id"),
                            title = o.getString("title"),
                            meta = o.getString("meta"),
                            dates = o.getString("dates"),
                            totalPrice = o.getInt("totalPrice"),
                            pickUpMillis = o.getLong("pickUpMillis"),
                            createdAtMillis = o.optLong("createdAtMillis", 0L),
                            status = o.optString("status", "Aktif")
                        )
                    )
                }
            }
        } catch (_: Throwable) {
            emptyList()
        }
    }

    private fun saveAll(context: Context, list: List<ReservationRecord>) {
        val arr = JSONArray()
        list.forEach { r ->
            arr.put(
                JSONObject().apply {
                    put("id", r.id)
                    put("title", r.title)
                    put("meta", r.meta)
                    put("dates", r.dates)
                    put("totalPrice", r.totalPrice)
                    put("pickUpMillis", r.pickUpMillis)
                    put("createdAtMillis", r.createdAtMillis)
                    put("status", r.status)
                }
            )
        }

        val sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        sp.edit().putString(KEY, arr.toString()).apply()
    }
}
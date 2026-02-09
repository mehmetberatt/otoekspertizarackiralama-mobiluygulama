package com.biturver.app.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VeritabaniYardimcisi(context: Context) : SQLiteOpenHelper(context, "BiTurVerDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val tabloOlustur = "CREATE TABLE gecmis (id INTEGER PRIMARY KEY AUTOINCREMENT, sonuc TEXT, tarih TEXT)"
        db?.execSQL(tabloOlustur)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS gecmis")
        onCreate(db)
    }

    fun kayitEkle(sonuc: String) {
        val db = this.writableDatabase
        val values = ContentValues()

        val tarihFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val tarih = tarihFormat.format(Date())

        values.put("sonuc", sonuc)
        values.put("tarih", tarih)

        db.insert("gecmis", null, values)
        db.close()
    }
}
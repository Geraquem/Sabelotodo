package com.mmfsin.sabelotodo.presentation.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.mmfsin.sabelotodo.R

class MainHelper(private val context: AppCompatActivity) {

    fun shareInfo(): Intent {
        val records = getAllRecords()

        val title = "¡Mis puntuaciones en Sabelotodo!\n\n"
        val text = "Edades de famosos españoles y latinos: ${records[0]}\n" +
                "Edades de famosos mundiales: ${records[1]}\n" +
                "Películas y series: ${records[2]}\n" +
                "Dibujos y animación: ${records[3]}\n" +
                "Videojuegos: ${records[4]}\n" +
                "Ferchas importantes: ${records[5]}\n\n"
        val url = "https://play.google.com/store/apps/developer?id=mmfsin&hl=es&gl=US"

        val completedMessage = title + text + url

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, completedMessage)
            type = "text/plain"
        }
        return Intent.createChooser(sendIntent, null)
    }

    private fun getAllRecords(): List<String> {
        val categories = mutableListOf<String>().apply {
            add(context.getString(R.string.spanish_age))
            add(context.getString(R.string.global_age))
            add(context.getString(R.string.films_series))
            add(context.getString(R.string.cartoon_creations))
            add(context.getString(R.string.videogames))
            add(context.getString(R.string.important_dates))
        }
        val records = mutableListOf<String>()
        for (category in categories) {
            records.add(getRecord(category).toString())
        }
        return records
    }

    fun getRecord(category: String): Int {
        val sharedPref = context.getPreferences(Context.MODE_PRIVATE) ?: return 1
        return sharedPref.getInt(category, 0)
    }
}
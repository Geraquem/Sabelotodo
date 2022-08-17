package com.mmfsin.sabelotodo.presentation.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.mmfsin.sabelotodo.R

class MainHelper(private val context: AppCompatActivity) {

    fun shareInfo(): Intent {

        /** FALTA HACER AQUI EL TEXTO QUE SE VA A ENVIAR */

        val records = getAllRecords()

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, records.toString())
            type = "text/plain"
        }
        return Intent.createChooser(sendIntent, null)
    }

    fun getAllRecords(): List<String> {
        val categories = mutableListOf<String>().apply {
            add(context.getString(R.string.spanish_age))
            add(context.getString(R.string.global_age))
            add(context.getString(R.string.films_series))
            add(context.getString(R.string.cartoon_creations))
            add(context.getString(R.string.videogames))
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
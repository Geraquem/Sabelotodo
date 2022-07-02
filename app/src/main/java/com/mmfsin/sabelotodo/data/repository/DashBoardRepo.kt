package com.mmfsin.sabelotodo.data.repository

import android.widget.Toast
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.sabelotodo.data.models.DataDTO

class DashBoardRepo(private var listener: IDashboardRepo) {

    fun getData() {
        val asasas = 1
        Firebase.database.reference.child("hola").get()
             .addOnSuccessListener {
                var a = 2
//  s              it.getValue(MusicVideoDTO::class.java)?.let { it1 -> listener.musicVideoData(it1) }

            }.addOnFailureListener {
                listener.somethingWentWrong()
            }
    }

    interface IDashboardRepo {
        fun setData(data: DataDTO)
        fun somethingWentWrong()
    }
}
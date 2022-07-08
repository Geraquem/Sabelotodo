package com.mmfsin.sabelotodo.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.sabelotodo.data.models.DataDTO

class DashboardRepo(private var listener: IDashboardRepo) {

    fun getData() {
        Firebase.database.reference.child("categories").get().addOnSuccessListener {
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
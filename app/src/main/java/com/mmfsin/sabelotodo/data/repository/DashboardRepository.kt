package com.mmfsin.sabelotodo.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.sabelotodo.data.database.RealmDatabase
import com.mmfsin.sabelotodo.domain.models.DataDTO

class DashboardRepository(private var listener: IDashboardRepo) {

    private val reference = Firebase.database.reference.child("questions")

    fun getDataFromFirebase(category: String) {
        val dataList = mutableListOf<DataDTO>()
        reference.child(category).get().addOnSuccessListener {
            for (child in it.children) {
                child.getValue(DataDTO::class.java)?.let { data -> dataList.add(data) }
            }
            listener.dataListFilled(dataList.shuffled())

        }.addOnFailureListener {
            listener.somethingWentWrong()
        }
    }

    interface IDashboardRepo {
        fun dataListFilled(list: List<DataDTO>)
        fun somethingWentWrong()
    }
}
package com.mmfsin.sabelotodo.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.sabelotodo.data.database.RealmDatabase
import com.mmfsin.sabelotodo.domain.models.CategoryDTO
import com.mmfsin.sabelotodo.domain.models.DataDTO
import io.realm.kotlin.where

class DashboardRepo(private var listener: IDashboardRepo) {

    private val realm by lazy { RealmDatabase() }

    private val reference = Firebase.database.reference.child("questions")

    fun getDataFromFirebase(category: String) {
        reference.child(category).get().addOnSuccessListener {
            for (child in it.children) {
                child.getValue(DataDTO::class.java)?.let { data -> saveData(data) }
            }
            listener.dataListFilled(getCompletedInfo().shuffled())

        }.addOnFailureListener {
            listener.somethingWentWrong()
        }
    }

    private fun getCompletedInfo(): List<DataDTO> {
        val list = realm.getObjectsFromRealm {
            where<DataDTO>().findAll()
        }
        return list.shuffled()
    }

    fun getCategoryImage(categoryName: String): String? {
        val categories = realm.getObjectsFromRealm {
            where<CategoryDTO>().findAll()
        }
        for (category in categories) {
            if (category.name == categoryName) return category.duckImage
        }
        return null
    }

    private fun saveData(data: DataDTO) = realm.addObject { data }

    interface IDashboardRepo {
        fun dataListFilled(list: List<DataDTO>)
        fun somethingWentWrong()
    }
}
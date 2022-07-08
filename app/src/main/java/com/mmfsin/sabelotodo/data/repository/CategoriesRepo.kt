package com.mmfsin.sabelotodo.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.sabelotodo.data.models.CategoryDTO

class CategoriesRepo(private var listener: ICategoriesRepo) {

    private val reference = Firebase.database.reference.child("categories")

    fun getCategoriesData() {
        reference.get().addOnSuccessListener {
            var a = 2
//  s              it.getValue(MusicVideoDTO::class.java)?.let { it1 -> listener.musicVideoData(it1) }

        }.addOnFailureListener {
            listener.somethingWentWrong()
        }
    }

    interface ICategoriesRepo {
        fun setCategoriesData(data: List<CategoryDTO>)
        fun somethingWentWrong()
    }
}
package com.mmfsin.sabelotodo.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.sabelotodo.data.models.CategoryDTO

class CategoriesRepo(private var listener: ICategoriesRepo) {

    private val reference = Firebase.database.reference.child("categories")

    fun getCategoriesData() {
        reference.get().addOnSuccessListener {
            val categories = mutableListOf<CategoryDTO>()
            for (child in it.children) {
                child.getValue(CategoryDTO::class.java)?.let { category ->
                    categories.add(category)
                }
            }
            listener.setCategoriesData(categories)

        }.addOnFailureListener {
            listener.somethingWentWrong()
        }
    }

    interface ICategoriesRepo {
        fun setCategoriesData(categories: List<CategoryDTO>)
        fun somethingWentWrong()
    }
}
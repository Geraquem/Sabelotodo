package com.mmfsin.sabelotodo.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.sabelotodo.data.database.RealmDatabase
import com.mmfsin.sabelotodo.domain.models.CategoryDTO
import com.mmfsin.sabelotodo.utils.CATEGORY_SIZE
import io.realm.kotlin.where

class CategoriesRepo(private var listener: ICategoriesRepo) {

    private val realm by lazy { RealmDatabase() }

    private val reference = Firebase.database.reference.child("categories")

    fun getCategories() {
        val categories = realm.getObjectsFromRealm {
            where<CategoryDTO>().findAll()
        }
        if (categories.size < CATEGORY_SIZE) getCategoriesDataFromFirebase()
        else listener.setCategoriesData(categories)
    }

    private fun getCategoriesDataFromFirebase() {
        reference.get().addOnSuccessListener {
            for (child in it.children) {
                child.getValue(CategoryDTO::class.java)?.let { category ->
                    saveCategory(category)
                }
            }
            getCategories()

        }.addOnFailureListener {
            listener.somethingWentWrong()
        }
    }

    private fun saveCategory(category: CategoryDTO) = realm.addObject { category }

    interface ICategoriesRepo {
        fun setCategoriesData(categories: List<CategoryDTO>)
        fun somethingWentWrong()
    }
}
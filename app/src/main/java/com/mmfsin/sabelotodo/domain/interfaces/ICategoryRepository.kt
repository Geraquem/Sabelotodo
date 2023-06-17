package com.mmfsin.sabelotodo.domain.interfaces

import com.mmfsin.sabelotodo.domain.models.Category

interface ICategoryRepository {
    fun getCategoriesFromRealm(): List<Category>
    fun getCategoryFromRealm(id: String): Category?
    suspend fun getCategoriesFromFirebase(): List<Category>
}
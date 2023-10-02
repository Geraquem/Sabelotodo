package com.mmfsin.sabelotodo.domain.interfaces

import com.mmfsin.sabelotodo.domain.models.Category

interface ICategoryRepository {
    fun getCategoriesFromRealm(): List<Category>
    suspend fun getCategories(): List<Category>
    fun getCategory(id: String): Category?

    fun getAvailableMusicMaster(): Boolean
}
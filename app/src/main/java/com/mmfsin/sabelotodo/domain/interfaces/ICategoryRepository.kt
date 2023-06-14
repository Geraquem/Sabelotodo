package com.mmfsin.sabelotodo.domain.interfaces

import com.mmfsin.sabelotodo.domain.models.CategoryDTO

interface ICategoryRepository {
    fun getCategoriesData(): List<CategoryDTO>
}
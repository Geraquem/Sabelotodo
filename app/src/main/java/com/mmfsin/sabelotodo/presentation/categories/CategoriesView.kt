package com.mmfsin.sabelotodo.presentation.categories

import com.mmfsin.sabelotodo.data.models.CategoryDTO

interface CategoriesView {
    fun setCategoriesData(categories: List<CategoryDTO>)
    fun somethingWentWrong()
}
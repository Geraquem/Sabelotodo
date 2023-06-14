package com.mmfsin.sabelotodo.presentation.categories

import com.mmfsin.sabelotodo.domain.models.CategoryDTO

sealed class CategoriesEvent {
    class Categories(val result: List<CategoryDTO>) : CategoriesEvent()
    object SomethingWentWrong : CategoriesEvent()
}
package com.mmfsin.sabelotodo.presentation.categories

import com.mmfsin.sabelotodo.domain.models.Category

sealed class CategoriesEvent {
    class Categories(val result: List<Category>) : CategoriesEvent()
    object SomethingWentWrong : CategoriesEvent()
}
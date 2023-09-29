package com.mmfsin.sabelotodo.presentation.categories.dialogs.category

import com.mmfsin.sabelotodo.domain.models.Category

sealed class CategoryDialogEvent {
    class GetCategory(val category: Category) : CategoryDialogEvent()
    object SomethingWentWrong : CategoryDialogEvent()
}
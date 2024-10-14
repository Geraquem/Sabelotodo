package com.mmfsin.sabelotodo.presentation.categories.dialogs.category

import com.mmfsin.sabelotodo.domain.models.Category

sealed class CategoryBSheetEvent {
    class GetCategory(val category: Category) : CategoryBSheetEvent()
    class AvailableMusicMaster(val available: Boolean) : CategoryBSheetEvent()
    object SomethingWentWrong : CategoryBSheetEvent()
}
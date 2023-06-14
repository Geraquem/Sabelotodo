package com.mmfsin.sabelotodo.presentation.categories

import com.mmfsin.sabelotodo.base.BaseViewModel
import com.mmfsin.sabelotodo.domain.usecases.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : BaseViewModel<CategoriesEvent>() {

    fun getCategories() {
        executeUseCase(
            { getCategoriesUseCase.execute() },
            { result -> _event.value = CategoriesEvent.Categories(result) },
            { _event.value = CategoriesEvent.SomethingWentWrong }
        )
    }
}
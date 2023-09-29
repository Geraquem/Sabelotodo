package com.mmfsin.sabelotodo.presentation.categories

import com.mmfsin.sabelotodo.base.BaseViewModel
import com.mmfsin.sabelotodo.domain.usecases.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : BaseViewModel<CategoriesEvent>() {

    fun getCategories(fromRealm: Boolean) {
        executeUseCase(
            { getCategoriesUseCase.execute(GetCategoriesUseCase.Params(fromRealm)) },
            { result ->
                _event.value = if (result.isNotEmpty()) CategoriesEvent.Categories(result)
                else CategoriesEvent.SomethingWentWrong
            },
            { _event.value = CategoriesEvent.SomethingWentWrong }
        )
    }
}
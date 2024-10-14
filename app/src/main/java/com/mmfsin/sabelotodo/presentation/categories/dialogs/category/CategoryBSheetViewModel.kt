package com.mmfsin.sabelotodo.presentation.categories.dialogs.category

import com.mmfsin.sabelotodo.base.BaseViewModel
import com.mmfsin.sabelotodo.domain.usecases.GetAvailableMusicMasterUseCase
import com.mmfsin.sabelotodo.domain.usecases.GetCategoryByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryBSheetViewModel @Inject constructor(
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getAvailableMusicMasterUseCase: GetAvailableMusicMasterUseCase
) : BaseViewModel<CategoryBSheetEvent>() {

    fun getCategory(id: String) {
        executeUseCase(
            { getCategoryByIdUseCase.execute(GetCategoryByIdUseCase.Params(id)) },
            { result ->
                _event.value = result?.let { CategoryBSheetEvent.GetCategory(result) }
                    ?: run { CategoryBSheetEvent.SomethingWentWrong }
            },
            { _event.value = CategoryBSheetEvent.SomethingWentWrong }
        )
    }

    fun checkIfAvailable() {
        executeUseCase(
            { getAvailableMusicMasterUseCase.execute() },
            { result -> _event.value = CategoryBSheetEvent.AvailableMusicMaster(result) },
            { _event.value = CategoryBSheetEvent.SomethingWentWrong }
        )
    }
}
package com.mmfsin.sabelotodo.presentation.dashboard

import com.mmfsin.sabelotodo.base.BaseViewModel
import com.mmfsin.sabelotodo.domain.usecases.GetCategoryByIdUseCase
import com.mmfsin.sabelotodo.domain.usecases.GetDashboardDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getDashboardDataUseCase: GetDashboardDataUseCase

) : BaseViewModel<DashboardEvent>() {

    fun getCategory(id: String) {
        executeUseCase(
            { getCategoryByIdUseCase.execute(GetCategoryByIdUseCase.Params(id)) },
            { result ->
                _event.value = result?.let { DashboardEvent.GetCategory(it) }
                    ?: run { DashboardEvent.SomethingWentWrong }
            },
            { _event.value = DashboardEvent.SomethingWentWrong }
        )
    }

    fun getDashboardData(categoryId: String) {
        executeUseCase(
            { getDashboardDataUseCase.execute(GetDashboardDataUseCase.Params(categoryId)) },
            { result -> _event.value = DashboardEvent.DashboardData(result) },
            { _event.value = DashboardEvent.SomethingWentWrong }
        )
    }
}
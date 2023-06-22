package com.mmfsin.sabelotodo.presentation.dashboard

import com.mmfsin.sabelotodo.base.BaseViewModel
import com.mmfsin.sabelotodo.domain.usecases.CheckRecordUseCase
import com.mmfsin.sabelotodo.domain.usecases.CheckSolutionUseCase
import com.mmfsin.sabelotodo.domain.usecases.GetCategoryByIdUseCase
import com.mmfsin.sabelotodo.domain.usecases.GetDashboardDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val checkSolutionUseCase: CheckSolutionUseCase,
    private val checkRecordUseCase: CheckRecordUseCase
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

    fun checkSolution(answer: String, solution: String) {
        executeUseCase(
            { checkSolutionUseCase.execute(CheckSolutionUseCase.Params(answer, solution)) },
            { result ->
                _event.value = result?.let { DashboardEvent.Solution(it) }
                    ?: run { DashboardEvent.SomethingWentWrong }
            },
            { _event.value = DashboardEvent.SomethingWentWrong }
        )
    }

    fun checkRecord(points: String, record: String, categoryId: String) {
        executeUseCase(
            { checkRecordUseCase.execute(CheckRecordUseCase.Params(points, record, categoryId)) },
            { result ->
                _event.value = result?.let { DashboardEvent.IsRecord(it) }
                    ?: run { DashboardEvent.SomethingWentWrong }
            },
            { _event.value = DashboardEvent.SomethingWentWrong }
        )
    }
}
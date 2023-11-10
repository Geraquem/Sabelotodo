package com.mmfsin.sabelotodo.presentation.dashboard.guesser

import com.mmfsin.sabelotodo.base.BaseViewModel
import com.mmfsin.sabelotodo.domain.usecases.CheckGuesserRecordUseCase
import com.mmfsin.sabelotodo.domain.usecases.CheckGuesserSolutionUseCase
import com.mmfsin.sabelotodo.domain.usecases.GetCategoryByIdUseCase
import com.mmfsin.sabelotodo.domain.usecases.GetDashboardDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GuesserViewModel @Inject constructor(
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val checkGuesserSolutionUseCase: CheckGuesserSolutionUseCase,
    private val checkGuesserRecordUseCase: CheckGuesserRecordUseCase
) : BaseViewModel<GuesserEvent>() {

    fun getCategory(id: String) {
        executeUseCase(
            { getCategoryByIdUseCase.execute(GetCategoryByIdUseCase.Params(id)) },
            { result ->
                _event.value = result?.let { GuesserEvent.GetCategory(it) }
                    ?: run { GuesserEvent.SomethingWentWrong }
            },
            { _event.value = GuesserEvent.SomethingWentWrong }
        )
    }

    fun getDashboardData(categoryId: String) {
        executeUseCase(
            { getDashboardDataUseCase.execute(GetDashboardDataUseCase.Params(categoryId)) },
            { result -> _event.value = GuesserEvent.GuesserData(result) },
            { _event.value = GuesserEvent.SomethingWentWrong }
        )
    }

    fun checkSolution(answer: String, solution: String) {
        executeUseCase(
            { checkGuesserSolutionUseCase.execute(CheckGuesserSolutionUseCase.Params(answer, solution)) },
            { result ->
                _event.value = result?.let { GuesserEvent.Solution(it) }
                    ?: run { GuesserEvent.SomethingWentWrong }
            },
            { _event.value = GuesserEvent.SomethingWentWrong }
        )
    }

    fun checkRecord(points: String, record: String, categoryId: String) {
        executeUseCase(
            { checkGuesserRecordUseCase.execute(CheckGuesserRecordUseCase.Params(points, record, categoryId)) },
            { result ->
                _event.value = result?.let { GuesserEvent.IsRecord(it) }
                    ?: run { GuesserEvent.SomethingWentWrong }
            },
            { _event.value = GuesserEvent.SomethingWentWrong }
        )
    }
}
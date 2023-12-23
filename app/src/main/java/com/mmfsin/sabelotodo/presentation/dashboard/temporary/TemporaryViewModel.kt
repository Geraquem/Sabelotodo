package com.mmfsin.sabelotodo.presentation.dashboard.temporary

import com.mmfsin.sabelotodo.base.BaseViewModel
import com.mmfsin.sabelotodo.domain.usecases.CheckTemporaryRecordUseCase
import com.mmfsin.sabelotodo.domain.usecases.CheckTemporarySolutionUseCase
import com.mmfsin.sabelotodo.domain.usecases.GetCategoryByIdUseCase
import com.mmfsin.sabelotodo.domain.usecases.GetDashboardDataUseCase
import com.mmfsin.sabelotodo.domain.usecases.GetImagesMeasuresUseCase
import com.mmfsin.sabelotodo.presentation.models.GamesType.TEMPORARY
import com.mmfsin.sabelotodo.presentation.models.TempSelectionType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TemporaryViewModel @Inject constructor(
    private val getImagesMeasuresUseCase: GetImagesMeasuresUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val checkTemporarySolutionUseCase: CheckTemporarySolutionUseCase,
    private val checkTemporaryRecordUseCase: CheckTemporaryRecordUseCase
) : BaseViewModel<TemporaryEvent>() {

    fun checkIfTablet() {
        executeUseCase(
            { getImagesMeasuresUseCase.execute(GetImagesMeasuresUseCase.Params(TEMPORARY)) },
            { result -> _event.value = TemporaryEvent.ImageHeight(result) },
            { _event.value = TemporaryEvent.SomethingWentWrong }
        )
    }

    fun getCategory(id: String) {
        executeUseCase(
            { getCategoryByIdUseCase.execute(GetCategoryByIdUseCase.Params(id)) },
            { result ->
                _event.value = result?.let { TemporaryEvent.GetCategory(it) }
                    ?: run { TemporaryEvent.SomethingWentWrong }
            },
            { _event.value = TemporaryEvent.SomethingWentWrong }
        )
    }

    fun getDashboardData(categoryId: String) {
        executeUseCase(
            { getDashboardDataUseCase.execute(GetDashboardDataUseCase.Params(categoryId)) },
            { result -> _event.value = TemporaryEvent.GuesserData(result) },
            { _event.value = TemporaryEvent.SomethingWentWrong }
        )
    }

    fun checkSolutions(answer: TempSelectionType, sol1: String, sol2: String) {
        executeUseCase({
            checkTemporarySolutionUseCase.execute(
                CheckTemporarySolutionUseCase.Params(answer, sol1, sol2)
            )
        }, { result ->
            _event.value = result?.let { TemporaryEvent.Solution(it) }
                ?: run { TemporaryEvent.SomethingWentWrong }
        }, { _event.value = TemporaryEvent.SomethingWentWrong })
    }

    fun checkRecord(points: String, record: String, categoryId: String) {
        executeUseCase(
            {
                checkTemporaryRecordUseCase.execute(
                    CheckTemporaryRecordUseCase.Params(
                        points,
                        record,
                        categoryId
                    )
                )
            },
            { result ->
                _event.value = result?.let { TemporaryEvent.IsRecord(it) }
                    ?: run { TemporaryEvent.SomethingWentWrong }
            },
            { _event.value = TemporaryEvent.SomethingWentWrong }
        )
    }
}
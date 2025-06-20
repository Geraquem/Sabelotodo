package com.mmfsin.sabelotodo.domain.usecases

import com.mmfsin.sabelotodo.base.BaseUseCase
import com.mmfsin.sabelotodo.presentation.models.ResultType
import com.mmfsin.sabelotodo.presentation.models.ResultType.BAD
import com.mmfsin.sabelotodo.presentation.models.ResultType.GOOD
import com.mmfsin.sabelotodo.presentation.models.TempSelectionType
import com.mmfsin.sabelotodo.presentation.models.TempSelectionType.BOTTOM
import com.mmfsin.sabelotodo.presentation.models.TempSelectionType.SAME_YEAR
import com.mmfsin.sabelotodo.presentation.models.TempSelectionType.TOP
import javax.inject.Inject

class CheckTemporarySolutionUseCase @Inject constructor() :
    BaseUseCase<CheckTemporarySolutionUseCase.Params, Pair<TempSelectionType, ResultType>?>() {

    override suspend fun execute(params: Params): Pair<TempSelectionType, ResultType>? {
        return try {
            val sol1 = params.solution1.toInt()
            val sol2 = params.solution2.toInt()

            val resultType = when (params.answer) {
                TOP -> {
                    if (sol1 < sol2) GOOD
                    else if (sol1 == sol2) ResultType.SAME_YEAR
                    else BAD
                }

                BOTTOM -> {
                    if (sol1 > sol2) GOOD
                    else if (sol1 == sol2) ResultType.SAME_YEAR
                    else BAD
                }

                SAME_YEAR -> if (sol1 == sol2) GOOD else BAD
            }
            Pair(params.answer, resultType)

        } catch (e: Exception) {
            null
        }
    }

    data class Params(
        val answer: TempSelectionType,
        val solution1: String,
        val solution2: String
    )
}
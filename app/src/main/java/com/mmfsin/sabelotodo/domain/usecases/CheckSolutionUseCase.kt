package com.mmfsin.sabelotodo.domain.usecases

import com.mmfsin.sabelotodo.base.BaseUseCase
import com.mmfsin.sabelotodo.domain.models.ResultType
import com.mmfsin.sabelotodo.domain.models.ResultType.*
import javax.inject.Inject
import kotlin.math.absoluteValue

class CheckSolutionUseCase @Inject constructor() :
    BaseUseCase<CheckSolutionUseCase.Params, ResultType?>() {

    override suspend fun execute(params: Params): ResultType? {
        return try {
            val answer = params.answer.toInt()
            val solution = params.solution.toInt()
            when ((answer - solution).absoluteValue) {
                0 -> GOOD
                1, 2, 3 -> ALMOST_GOOD
                else -> BAD
            }
        } catch (e: Exception) {
            null
        }
    }

    data class Params(val answer: String, val solution: String)
}
package com.mmfsin.sabelotodo.domain.usecases

import com.mmfsin.sabelotodo.base.BaseUseCase
import com.mmfsin.sabelotodo.domain.interfaces.IDashboardRepository
import javax.inject.Inject

class CheckRecordUseCase @Inject constructor(private val repository: IDashboardRepository) :
    BaseUseCase<CheckRecordUseCase.Params, Boolean?>() {

    override suspend fun execute(params: Params): Boolean? {
        return try {
            val points = params.points.toInt()
            val record = params.record.toInt()
            return if (points > 0 && points > record) {
                repository.updateRecord(params.categoryId, points)
                true
            } else false
        } catch (e: Exception) {
            null
        }
    }

    data class Params(
        val points: String,
        val record: String,
        val categoryId: String
    )
}
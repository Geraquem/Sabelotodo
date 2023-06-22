package com.mmfsin.sabelotodo.domain.usecases

import com.mmfsin.sabelotodo.base.BaseUseCase
import com.mmfsin.sabelotodo.domain.interfaces.IDashboardRepository
import com.mmfsin.sabelotodo.domain.models.Record
import javax.inject.Inject

class CheckRecordUseCase @Inject constructor(private val repository: IDashboardRepository) :
    BaseUseCase<CheckRecordUseCase.Params, Record?>() {

    override suspend fun execute(params: Params): Record? {
        return try {
            val points = params.points.toInt()
            val record = params.record.toInt()
            val isRecord = if (points > 0 && points > record) {
                repository.updateRecord(params.categoryId, points)
                true
            } else false
            Record(isRecord, points)
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
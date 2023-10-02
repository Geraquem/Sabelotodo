package com.mmfsin.sabelotodo.domain.usecases

import com.mmfsin.sabelotodo.base.BaseUseCase
import com.mmfsin.sabelotodo.domain.interfaces.IDashboardRepository
import com.mmfsin.sabelotodo.domain.models.Data
import javax.inject.Inject

class GetDashboardDataUseCase @Inject constructor(private val repository: IDashboardRepository) :
    BaseUseCase<GetDashboardDataUseCase.Params, List<Data>>() {

    override suspend fun execute(params: Params): List<Data> {
        val data = repository.getDashboardData(params.categoryId)
        return try {
            val time = System.currentTimeMillis()
            val timeStr = time.toString()
            var lastPosition = timeStr.substring(timeStr.length - 1).toInt()

            if (lastPosition > 3) lastPosition = 3
            for (i in 1..lastPosition) {
                data.shuffled()
            }
            data.shuffled()

        } catch (e: Exception) {
            data.shuffled()
        }
    }

    data class Params(val categoryId: String)
}
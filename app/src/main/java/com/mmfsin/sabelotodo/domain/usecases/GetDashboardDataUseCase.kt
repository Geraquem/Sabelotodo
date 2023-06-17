package com.mmfsin.sabelotodo.domain.usecases

import com.mmfsin.sabelotodo.base.BaseUseCase
import com.mmfsin.sabelotodo.domain.interfaces.IDashboardRepository
import com.mmfsin.sabelotodo.domain.models.Data
import javax.inject.Inject

class GetDashboardDataUseCase @Inject constructor(private val repository: IDashboardRepository) :
    BaseUseCase<GetDashboardDataUseCase.Params, List<Data>>() {

    override suspend fun execute(params: Params): List<Data> =
        repository.getDashboardData(params.categoryId)

    data class Params(val categoryId: String)
}
package com.mmfsin.sabelotodo.domain.usecases

import com.mmfsin.sabelotodo.base.BaseUseCaseNoParams
import com.mmfsin.sabelotodo.domain.interfaces.IDashboardRepository
import javax.inject.Inject

class GetLoserImagesUseCase @Inject constructor(
    private val repository: IDashboardRepository
) : BaseUseCaseNoParams<String>() {

    override suspend fun execute(): String {
        val images = repository.getLoserImages()
        return if (images.isEmpty()) ""
        else images.random().image
    }
}
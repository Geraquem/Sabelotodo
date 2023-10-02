package com.mmfsin.sabelotodo.domain.usecases

import com.mmfsin.sabelotodo.base.BaseUseCaseNoParams
import com.mmfsin.sabelotodo.domain.interfaces.ICategoryRepository
import com.mmfsin.sabelotodo.domain.models.Category
import javax.inject.Inject

class GetAvailableMusicMasterUseCase @Inject constructor(private val repository: ICategoryRepository) :
    BaseUseCaseNoParams<Boolean>() {

    override suspend fun execute(): Boolean = repository.getAvailableMusicMaster()
}
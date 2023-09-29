package com.mmfsin.sabelotodo.domain.usecases

import com.mmfsin.sabelotodo.base.BaseUseCase
import com.mmfsin.sabelotodo.domain.interfaces.ICategoryRepository
import com.mmfsin.sabelotodo.domain.models.Category
import javax.inject.Inject

class GetCategoryByIdUseCase @Inject constructor(private val repository: ICategoryRepository) :
    BaseUseCase<GetCategoryByIdUseCase.Params, Category?>() {

    override suspend fun execute(params: Params): Category? = repository.getCategory(params.id)

    data class Params(val id: String)
}
package com.mmfsin.sabelotodo.domain.usecases

import com.mmfsin.sabelotodo.base.BaseUseCase
import com.mmfsin.sabelotodo.domain.interfaces.ICategoryRepository
import com.mmfsin.sabelotodo.domain.models.Category
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val repository: ICategoryRepository) :
    BaseUseCase<GetCategoriesUseCase.Params, List<Category>>() {

    override suspend fun execute(params: Params): List<Category> {
        return if (params.fromRealm) repository.getCategoriesFromRealm()
        else repository.getCategories()
    }

    data class Params(
        val fromRealm: Boolean
    )
}
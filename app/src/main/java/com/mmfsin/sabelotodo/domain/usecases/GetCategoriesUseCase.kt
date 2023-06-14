package com.mmfsin.sabelotodo.domain.usecases

import com.mmfsin.sabelotodo.base.BaseUseCaseNoParams
import com.mmfsin.sabelotodo.domain.interfaces.ICategoryRepository
import com.mmfsin.sabelotodo.domain.models.CategoryDTO
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val repository: ICategoryRepository) :
    BaseUseCaseNoParams<List<CategoryDTO>>() {

    override suspend fun execute(): List<CategoryDTO> {
        /** do login here */
        return repository.getCategoriesData()
    }
}
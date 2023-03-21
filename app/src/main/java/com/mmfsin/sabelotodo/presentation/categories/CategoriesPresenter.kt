package com.mmfsin.sabelotodo.presentation.categories

import com.mmfsin.sabelotodo.domain.models.CategoryDTO
import com.mmfsin.sabelotodo.data.repository.CategoriesRepo
import com.mmfsin.sabelotodo.data.repository.CategoriesRepo.ICategoriesRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CategoriesPresenter(private val view: CategoriesView) : ICategoriesRepo, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private val repo by lazy { CategoriesRepo(this) }

    fun getCategoriesData() {
        launch(Dispatchers.IO) { repo.getCategories() }
    }

    override fun setCategoriesData(categories: List<CategoryDTO>) {
        launch { view.setCategoriesData(categories) }
    }

    override fun somethingWentWrong() {
        launch { view.somethingWentWrong() }
    }
}
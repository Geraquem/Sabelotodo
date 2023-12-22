package com.mmfsin.sabelotodo.presentation.categories.interfaces

import com.mmfsin.sabelotodo.domain.models.Category

interface ICategoryListener {
    fun onCategoryScrolled(category: Category)
    fun onCategoryClick(id: String)
    fun startGuesserGame(categoryId: String)
    fun startTemporaryGame(categoryId: String)
    fun openMusicMasterDialog(categoryId: String)
    fun openMusicMaster()
}
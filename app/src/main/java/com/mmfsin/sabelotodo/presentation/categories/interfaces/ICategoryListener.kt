package com.mmfsin.sabelotodo.presentation.categories.interfaces

interface ICategoryListener {
    fun onCategoryClick(id: String)
    fun startGuesserGame(categoryId: String)
    fun startCTemporaryGame(categoryId: String)
    fun openMusicMaster()
}
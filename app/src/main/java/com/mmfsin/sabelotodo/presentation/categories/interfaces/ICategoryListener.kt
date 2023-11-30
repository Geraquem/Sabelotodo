package com.mmfsin.sabelotodo.presentation.categories.interfaces

interface ICategoryListener {
    fun onCategoryScrolled(title: String, description: String)
    fun onCategoryClick(id: String)
    fun startGuesserGame(categoryId: String)
    fun startCTemporaryGame(categoryId: String)
    fun openMusicMaster()
}
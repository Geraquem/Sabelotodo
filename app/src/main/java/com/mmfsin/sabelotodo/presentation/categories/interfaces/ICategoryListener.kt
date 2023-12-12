package com.mmfsin.sabelotodo.presentation.categories.interfaces

interface ICategoryListener {
    fun onCategoryScrolled(id: String, title: String, description: String, color: String)
    fun onCategoryClick(id: String)
    fun startGuesserGame(categoryId: String)
    fun startCTemporaryGame(categoryId: String)
    fun openMusicMaster()
}
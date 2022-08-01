package com.mmfsin.sabelotodo.presentation

import com.mmfsin.sabelotodo.data.models.CategoryDTO

interface ICommunication {
    fun navigateToDashboard(category: CategoryDTO)
    fun changeToolbarText(category: String)
    fun closeKeyboard()
    fun somethingWentWrong()
}
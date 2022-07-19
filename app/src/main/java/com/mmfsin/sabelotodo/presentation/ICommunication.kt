package com.mmfsin.sabelotodo.presentation

import com.mmfsin.sabelotodo.data.models.DataToDash

interface ICommunication {
    fun navigateToDashboard(dataToDash: DataToDash)
    fun closeKeyboard()
    fun somethingWentWrong()
}
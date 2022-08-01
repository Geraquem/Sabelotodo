package com.mmfsin.sabelotodo.presentation

import com.mmfsin.sabelotodo.data.models.DataToDashDTO
import com.mmfsin.sabelotodo.data.models.RecordDTO

interface ICommunication {
    fun navigateToDashboard(data: DataToDashDTO)
    fun changeToolbarText(category: String)

    fun getRecord(category: String): Int
    fun setNewRecord(record: RecordDTO)

    fun closeKeyboard()
    fun somethingWentWrong()
}
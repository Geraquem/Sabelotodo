package com.mmfsin.sabelotodo.presentation

import com.mmfsin.sabelotodo.domain.models.DataToDashDTO
import com.mmfsin.sabelotodo.domain.models.RecordDTO

interface ICommunication {
    fun navigateToDashboard(data: DataToDashDTO)
    fun changeToolbarText(category: String)
    fun notMoreQuestions()

    fun getRecord(category: String): Int
    fun setNewRecord(record: RecordDTO)

    fun showAd(pos: Int)

    fun closeKeyboard()
    fun somethingWentWrong()
}
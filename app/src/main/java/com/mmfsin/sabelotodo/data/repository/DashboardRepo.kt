package com.mmfsin.sabelotodo.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mmfsin.sabelotodo.data.models.DataDTO

class DashboardRepo(private var listener: IDashboardRepo) {

    private val reference = Firebase.database.reference.child("questions")

    fun getDataList(category: String) {
        reference.child(category).get().addOnSuccessListener {
            val list = mutableListOf<String>()
            for (child in it.children) {
                list.add(child.key.toString())
            }
            listener.setDataList(list)

        }.addOnFailureListener {
            listener.somethingWentWrong()
        }
    }

    fun getQuestionData(category: String, questionName: String) {
        reference.child(category).child(questionName).get().addOnSuccessListener {
            it.getValue(DataDTO::class.java)?.let { data -> listener.setQuestionData(data) }

        }.addOnFailureListener {
            listener.somethingWentWrong()
        }
    }

    interface IDashboardRepo {
        fun setDataList(list: List<String>)
        fun setQuestionData(data: DataDTO)
        fun somethingWentWrong()
    }
}
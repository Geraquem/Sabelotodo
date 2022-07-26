package com.mmfsin.sabelotodo.presentation.dashboard

import com.mmfsin.sabelotodo.data.models.DataDTO
import com.mmfsin.sabelotodo.data.models.SolutionDTO
import com.mmfsin.sabelotodo.data.repository.DashboardRepo
import com.mmfsin.sabelotodo.data.repository.DashboardRepo.IDashboardRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DashboardPresenter(private val view: DashboardView) : IDashboardRepo, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private val repo by lazy { DashboardRepo(this) }

    fun getDataList(category: String) {
        launch(Dispatchers.IO) { repo.getDataList(category) }
    }

    fun getQuestionData(category: String, questionName: String) {
        launch(Dispatchers.IO) { repo.getQuestionData(category, questionName) }
    }

    fun checkDescription(description: String) {
        if (description == "null") view.handleDescription(false, "")
        else view.handleDescription(true, description)
    }

    fun checkSolution(solution: SolutionDTO) {
        val userAnswer = solution.userAnswer.toInt()
        val correct = solution.correctAnswer.toInt()

        val isCorrect = if (userAnswer == correct) {
            0
        } else if (userAnswer > (correct - 3) && userAnswer < (correct + 3) && userAnswer != correct) {
            1
        } else {
            2
        }
        view.showSolution(solution.correctAnswer, isCorrect)
    }

    override fun setDataList(list: List<String>) {
        launch { view.setDataList(list) }
    }

    override fun setQuestionData(data: DataDTO) {
        launch { view.setQuestionData(data) }
    }

    override fun somethingWentWrong() {
        launch { view.somethingWentWrong() }
    }
}
package com.mmfsin.sabelotodo.presentation.dashboard

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

//fun String.getMainText(): Pair<String, String> {}

//    private val repo by lazy { DashboardRepository(this) }

//    fun toolbarText(context: Context, category: String): String {
//        return with(context) {
//            when (category) {
//                getString(R.string.spanish_age) -> getString(R.string.spanish_age_toolbar)
//                getString(R.string.global_age) -> getString(R.string.global_age_toolbar)
//                getString(R.string.films_series) -> getString(R.string.films_series_toolbar)
//                getString(R.string.cartoon_creations) -> getString(R.string.cartoon_creations_toolbar)
//                getString(R.string.videogames) -> getString(R.string.videogames_toolbar)
//                getString(R.string.important_dates) -> getString(R.string.important_dates_toolbar)
//                else -> "UPSI"
//            }
//        }
//    }

//    fun checkPinViewLongitude(context: Context, category: String): Int {
//        val longitude = when (category) {
//            context.getString(R.string.spanish_age) -> 2
//            context.getString(R.string.global_age) -> 2
//            else -> 4
//        }
//        if (longitude == 2) view.setTwoLongitudePinView()
//        return longitude
//        return 4
//    }
//
//    fun getData(category: String) = launch(Dispatchers.IO) { repo.getDataFromFirebase(category) }
//
//    fun checkDescription(description: String) {
//        if (description == "null") view.handleDescription(false, "")
//        else view.handleDescription(true, description)
//    }
//
//    fun checkSolution(solution: SolutionDTO) {
//        val userAnswer = solution.userAnswer.toInt()
//        val correct = solution.correctAnswer.toInt()
//
//        val type = when ((userAnswer - correct).absoluteValue) {
//            0 -> GOOD
//            1 -> ALMOST_GOOD
//            2 -> ALMOST_GOOD
//            3 -> ALMOST_GOOD
//            else -> BAD
//        }
//        view.showSolution(solution.correctAnswer, type)
//    }
//
//    fun checkPinViewLongitude(longitude: Int, solution: String): Boolean {
//        return (solution.length == longitude)
//    }
//
//    fun checkSolution(solution: String): String {
//        return if (solution.contains("/")) calculateAge(solution.split("/"))
//        else solution
//    }
//
//    private fun calculateAge(age: List<String>): String {
//        return Period.between(
//            LocalDate.of(age[2].toInt(), age[1].toInt(), age[0].toInt()), LocalDate.now()
//        ).years.toString()
//    }
//
//    override fun dataListFilled(list: List<GameData>) {
//        launch { view.dataListFilled(list.shuffled()) }
//    }
//
//    fun getColorByCategory(c: Context, category: String): Int {
////        return when (category) {
////            c.getString(R.string.spanish_age) -> R.color.spa
////            c.getString(R.string.global_age) -> R.color.fam
////            c.getString(R.string.films_series) -> R.color.film
////            c.getString(R.string.cartoon_creations) -> R.color.anim
////            c.getString(R.string.videogames) -> R.color.video
////            c.getString(R.string.important_dates) -> R.color.g_12
////            else -> R.color.test
////        }
//        return R.color.g_12
//    }
//
//    override fun somethingWentWrong() {
//        launch { view.somethingWentWrong() }
//    }

package com.mmfsin.sabelotodo.data.mappers

import android.annotation.SuppressLint
import com.mmfsin.sabelotodo.data.models.CategoryDTO
import com.mmfsin.sabelotodo.data.models.DataDTO
import com.mmfsin.sabelotodo.data.models.UserRecordDTO
import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.domain.models.Data
import com.mmfsin.sabelotodo.domain.models.UserRecord
import java.text.SimpleDateFormat
import java.time.LocalDate.now
import java.time.LocalDate.of
import java.time.Period
import java.util.*
import java.util.Calendar.MONTH

fun DataDTO.toData() = Data(
    image = image,
    firstText = text.split("%%%")[0],
    secondText = text.split("%%%")[1],
    birth = solution.getBirth(),
    solution = solution.getSolution(),
    auxText = auxText
)

fun List<DataDTO>.toDataList() = this.map { element -> element.toData() }.toList()

@SuppressLint("SimpleDateFormat")
fun String.getBirth(): List<String> {
    if (this.contains("/")) {
        val age = this.split("/")
        try {
            val birth = mutableListOf<String>()

            /** DAY */
            val day = age[0]
            if (day.substring(0, 1) == "0") birth.add(day.substring(1, 2))
            else birth.add(day)
            /** MONTH */
            val birthMonth = age[1].toInt()
            val calendar = Calendar.getInstance()
            val monthDate = SimpleDateFormat("MMMM")
            calendar[MONTH] = birthMonth - 1
            val monthName: String = monthDate.format(calendar.time)
            birth.add(monthName)
            /** YEAR */
            birth.add(age[2])
            return birth
        } catch (e: java.lang.Exception) {
            return emptyList()
        }
    } else return emptyList()
}

fun String.getSolution(): String {
    return if (this.contains("/")) {
        val age = this.split("/")
        try {
            Period.between(
                of(age[2].toInt(), age[1].toInt(), age[0].toInt()), now()
            ).years.toString()
        } catch (e: java.lang.Exception) {
            return ""
        }
    } else this
}

fun CategoryDTO.toCategory(guesserRecord: Int, temporaryRecord: Int) = Category(
    id = id,
    title = title,
    image = image,
    duckImage = duckImage,
    shortDescription = shortDescription,
    description = description,
    examples = examples,
    colorDashboard = colorDashboard,
    colorStart = colorStart,
    colorEnd = colorEnd,
    order = order,
    toolbarText = toolbarText,
    longitudePV = longitudePV,
    mainImage = mainImage,
    guesserRecord = guesserRecord,
    temporaryRecord = temporaryRecord
)

//fun List<CategoryDTO>.toCategoryList() = this.map { element -> element.toCategory() }.toList()

fun UserRecordDTO.toUserRecord() = UserRecord(
    guesserRecord = guesserRecord ?: 0,
    temporaryRecord = temporaryRecord ?: 0
)

package com.mmfsin.sabelotodo.data.mappers

import com.mmfsin.sabelotodo.data.models.DataDTO
import com.mmfsin.sabelotodo.domain.models.Data
import java.time.LocalDate.now
import java.time.LocalDate.of
import java.time.Period

fun DataDTO.toData() = Data(
    image = image,
    firstText = text.split("%%%")[0],
    secondText = text.split("%%%")[1],
    solution = solution.getSolution()
)

fun List<DataDTO>.toDataList() = this.map { element -> element.toData() }.toList()

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

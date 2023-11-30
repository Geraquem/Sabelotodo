package com.mmfsin.sabelotodo.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CategoryDTO(
    @PrimaryKey
    var id: String = "",
    var title: String = "",
    var image: String = "",
    var duckImage: String = "",
    var shortDescription: String = "",
    var description: String = "",
    var examples: String = "",
    var colorDashboard: String = "#FFFFFFFF",
    var colorStart: String = "#FFFFFFFF",
    var colorEnd: String = "#FFFFFFFF",
    var order: Long = 0,
    var guesserRecord: Int? = 0,
    var temporaryRecord: Int? = 0,
    var toolbarText: String = "",
    var longitudePV: Int = 0,
    var mainImage: String = ""
) : RealmObject()

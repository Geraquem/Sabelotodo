package com.mmfsin.sabelotodo.domain.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Category(
    @PrimaryKey
    var id: String = "",
    var title: String = "",
    var image: String = "",
    var description: String = "",
    var colorStart: String = "#FFFFFFFF",
    var colorEnd: String = "#FFFFFFFF",
    var order: Long = 0,
    var record: Int? = 0,
    var longitudePV: Int = 0,
) : RealmObject()

package com.mmfsin.sabelotodo.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class CategoryDTO : RealmObject {
    @PrimaryKey
    var id: String = ""
    var title: String = ""
    var image: String = ""
    var duckImage: String = ""
    var shortDescription: String = ""
    var description: String = ""
    var examples: String = ""
    var colorDashboard: String = "#FFFFFFFF"
    var colorStart: String = "#FFFFFFFF"
    var colorEnd: String = "#FFFFFFFF"
    var order: Long = 0
    var toolbarText: String = ""
    var longitudePV: Int = 0
    var mainImage: String = ""
}

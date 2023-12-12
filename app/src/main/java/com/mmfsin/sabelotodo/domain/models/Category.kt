package com.mmfsin.sabelotodo.domain.models

open class Category(
    var id: String,
    var title: String,
    var image: String,
    var duckImage: String,
    var shortDescription: String,
    var description: String,
    var examples: String,
    var colorDashboard: String,
    var colorStart: String,
    var colorEnd: String,
    var order: Long,
    var toolbarText: String,
    var longitudePV: Int,
    var mainImage: String,
    var guesserRecord: Int?,
    var temporaryRecord: Int?,
)

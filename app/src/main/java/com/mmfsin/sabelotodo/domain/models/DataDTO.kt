package com.mmfsin.sabelotodo.domain.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DataDTO(
    @PrimaryKey
    var image: String = "",
    var text: String = "",
    var description: String = "",
    var solution: String = "",
) : RealmObject()

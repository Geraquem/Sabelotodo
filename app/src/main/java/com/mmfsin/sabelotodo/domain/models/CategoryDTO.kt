package com.mmfsin.sabelotodo.domain.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CategoryDTO(
    @PrimaryKey
    var name: String = "",
    var title: String = "",
    var image: String = "",
    var description: String = "",
) : RealmObject()

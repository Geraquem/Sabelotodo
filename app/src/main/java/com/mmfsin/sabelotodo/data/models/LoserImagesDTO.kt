package com.mmfsin.sabelotodo.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class LoserImagesDTO(
    @PrimaryKey
    var id: String = "",
    var image: String = ""
) : RealmObject()
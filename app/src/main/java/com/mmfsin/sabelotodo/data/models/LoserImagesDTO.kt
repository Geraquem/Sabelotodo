package com.mmfsin.sabelotodo.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class LoserImagesDTO : RealmObject {
    @PrimaryKey
    var id: String = ""
    var image: String = ""
}
package com.mmfsin.sabelotodo.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class UserRecordDTO : RealmObject {
    @PrimaryKey
    var id: String = ""
    var guesserRecord: Int? = 0
    var temporaryRecord: Int? = 0
}
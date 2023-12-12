package com.mmfsin.sabelotodo.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserRecordDTO(
    @PrimaryKey
    var id: String = "",
    var guesserRecord: Int? = 0,
    var temporaryRecord: Int? = 0
) : RealmObject()

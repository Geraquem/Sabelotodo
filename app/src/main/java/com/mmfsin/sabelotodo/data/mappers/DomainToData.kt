package com.mmfsin.sabelotodo.data.mappers

import com.mmfsin.sabelotodo.data.models.UserRecordDTO
import com.mmfsin.sabelotodo.domain.models.UserRecord

fun UserRecord.toUserRecordDTO(categoryId: String) = UserRecordDTO(
    id = categoryId,
    guesserRecord = guesserRecord,
    temporaryRecord = temporaryRecord
)
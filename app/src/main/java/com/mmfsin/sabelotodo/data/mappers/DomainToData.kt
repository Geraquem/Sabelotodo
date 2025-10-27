package com.mmfsin.sabelotodo.data.mappers

import com.mmfsin.sabelotodo.data.models.LoserImagesDTO
import com.mmfsin.sabelotodo.data.models.UserRecordDTO
import com.mmfsin.sabelotodo.domain.models.UserRecord

fun toUserRecordDTO(categoryId: String, ur: UserRecord) = UserRecordDTO().apply {
    id = categoryId
    guesserRecord = ur.guesserRecord
    temporaryRecord = ur.temporaryRecord
}

fun createLoserImageDTO(newId: String, newImage: String) = LoserImagesDTO().apply {
    id = newId
    image = newImage
}
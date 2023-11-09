package com.mmfsin.sabelotodo.presentation.dashboard.temporary

import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.domain.models.Data
import com.mmfsin.sabelotodo.domain.models.Record
import com.mmfsin.sabelotodo.domain.models.ResultType

sealed class TemporaryEvent {
    class GetCategory(val result: Category) : TemporaryEvent()
    class GuesserData(val data: List<Data>) : TemporaryEvent()
    class Solution(val solution: ResultType) : TemporaryEvent()
    class IsRecord(val result: Record) : TemporaryEvent()
    object SomethingWentWrong : TemporaryEvent()
}
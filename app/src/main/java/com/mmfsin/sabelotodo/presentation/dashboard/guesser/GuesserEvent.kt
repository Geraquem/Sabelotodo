package com.mmfsin.sabelotodo.presentation.dashboard.guesser

import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.domain.models.Data
import com.mmfsin.sabelotodo.domain.models.Record
import com.mmfsin.sabelotodo.domain.models.ResultType

sealed class GuesserEvent {
    class GetCategory(val result: Category) : GuesserEvent()
    class GuesserData(val data: List<Data>) : GuesserEvent()
    class Solution(val solution: ResultType) : GuesserEvent()
    class IsRecord(val result: Record) : GuesserEvent()
    object SomethingWentWrong : GuesserEvent()
}
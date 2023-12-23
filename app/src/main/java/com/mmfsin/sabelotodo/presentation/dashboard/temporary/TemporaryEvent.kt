package com.mmfsin.sabelotodo.presentation.dashboard.temporary

import com.mmfsin.sabelotodo.domain.models.Category
import com.mmfsin.sabelotodo.domain.models.Data
import com.mmfsin.sabelotodo.domain.models.Record
import com.mmfsin.sabelotodo.presentation.dashboard.guesser.GuesserEvent
import com.mmfsin.sabelotodo.presentation.models.ResultType
import com.mmfsin.sabelotodo.presentation.models.TempSelectionType

sealed class TemporaryEvent {
    class ImageHeight(val height: Int) : TemporaryEvent()
    class GetCategory(val result: Category) : TemporaryEvent()
    class GuesserData(val data: List<Data>) : TemporaryEvent()
    class Solution(val solution: Pair<TempSelectionType, ResultType>) : TemporaryEvent()
    class IsRecord(val result: Record) : TemporaryEvent()
    object SomethingWentWrong : TemporaryEvent()
}
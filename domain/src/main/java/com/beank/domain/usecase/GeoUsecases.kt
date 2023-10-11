package com.beank.domain.usecase

import com.beank.domain.usecase.geo.GetChooseTagId
import com.beank.domain.usecase.record.GetCurrentRecord
import com.beank.domain.usecase.record.InsertRecord
import com.beank.domain.usecase.record.UpdateRecord

data class GeoUsecases(
    val getCurrentRecord: GetCurrentRecord,
    val updateRecord: UpdateRecord,
    val insertRecord: InsertRecord,
    val getChooseTagId: GetChooseTagId
)
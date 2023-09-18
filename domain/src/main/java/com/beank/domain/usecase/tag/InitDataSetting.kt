package com.beank.domain.usecase.tag

import com.beank.domain.model.Record
import com.beank.domain.model.Tag
import com.beank.domain.usecase.record.InsertRecord
import javax.inject.Inject

class InitDataSetting @Inject constructor(
    private val insertRecord: InsertRecord,
    private val insertTag: InsertTag
) {
    operator fun invoke(){
        val tagName = listOf("공부중","운동중","휴식중","이동중","수면중","개인시간")
        tagName.forEachIndexed { index, s ->
            insertTag(Tag(null,index,s))
        }
        insertRecord(Record(tag = "개인시간"))
    }
}
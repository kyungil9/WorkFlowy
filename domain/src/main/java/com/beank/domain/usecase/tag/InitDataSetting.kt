package com.beank.domain.usecase.tag

import android.os.Build
import androidx.annotation.RequiresApi
import com.beank.domain.model.Record
import com.beank.domain.model.Tag
import com.beank.domain.usecase.record.InsertRecord
import com.beank.domain.usecase.user.InitUserInfo
import javax.inject.Inject

class InitDataSetting @Inject constructor(
    private val insertRecord: InsertRecord,
    private val insertTag: InsertTag,
    private val initUserInfo: InitUserInfo
) {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(){
        val tagName = listOf("공부중","운동중","휴식중","이동중","수면중","개인시간")
        tagName.forEachIndexed { index, s ->
            insertTag(Tag(null,index,s))
        }
        insertRecord(Record(tag = "개인시간"))
        initUserInfo()
    }
}
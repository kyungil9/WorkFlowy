package com.beank.domain.usecase

import com.beank.domain.usecase.tag.CheckTagTitle
import com.beank.domain.usecase.tag.InsertTag

data class TagUsecases(
    val checkTagTitle: CheckTagTitle,
    val insertTag: InsertTag
)

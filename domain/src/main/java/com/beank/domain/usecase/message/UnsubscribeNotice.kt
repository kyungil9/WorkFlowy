package com.beank.domain.usecase.message

import com.beank.domain.repository.MessageRepository
import javax.inject.Inject

class UnsubscribeNotice @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke() = messageRepository.unsubscribeNotice()
}
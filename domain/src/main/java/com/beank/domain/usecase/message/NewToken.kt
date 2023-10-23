package com.beank.domain.usecase.message

import com.beank.domain.repository.MessageRepository
import javax.inject.Inject

class NewToken @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(token : String) = messageRepository.newToken(token)
}
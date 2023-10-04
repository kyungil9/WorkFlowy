package com.beank.domain.usecase.user

import com.beank.domain.repository.UserRepository
import javax.inject.Inject

class InitUserInfo @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() = userRepository.initUserInfo()
}
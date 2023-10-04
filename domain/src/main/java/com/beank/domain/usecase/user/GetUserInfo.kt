package com.beank.domain.usecase.user

import com.beank.domain.repository.UserRepository
import javax.inject.Inject

class GetUserInfo @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() = userRepository.getUserInfo()
}
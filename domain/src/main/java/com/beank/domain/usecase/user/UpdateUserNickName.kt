package com.beank.domain.usecase.user

import com.beank.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserNickName @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(name : String) = userRepository.updateUserNickName(name)
}
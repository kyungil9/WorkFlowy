package com.beank.domain.usecase.user

import android.net.Uri
import com.beank.domain.repository.UserRepository
import javax.inject.Inject

class UploadImageUrl @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(uri: Uri, onFail : () -> Unit) = userRepository.uploadImageUrl(uri, onFail)
}
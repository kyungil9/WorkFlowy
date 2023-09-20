package com.beank.domain.model

import com.google.firebase.FirebaseException

sealed class FireStoreState<out T> {
    object Loading : FireStoreState<Nothing>()
    object Empty : FireStoreState<Nothing>()
    data class Success<out T>(val data: T) : FireStoreState<T>()
    data class Exception(val message: String?, val e: FirebaseException?) : FireStoreState<Nothing>()

}

inline fun <reified T : Any> FireStoreState<T>.onLoading(action: () -> Unit) {
    if (this is FireStoreState.Loading) action()
}

inline fun <reified T : Any> FireStoreState<T>.onEmpty(action: () -> Unit) {
    if (this is FireStoreState.Empty) action()
}

inline fun <reified T : Any> FireStoreState<T>.onSuccess(action: (data: T) -> Unit) {
    if (this is FireStoreState.Success) action(data)
}

inline fun <reified T : Any> FireStoreState<T>.onException(action: (message: String?,e: FirebaseException?) -> Unit) {
    if (this is FireStoreState.Exception) action(message,e)
}

package com.beank.domain.service

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow


interface AccountService {

    val hasUser : Boolean
    val currentUser : FirebaseUser?

    suspend fun createAccount(email : String, password : String, onSuccess : () -> Unit, onFailMessage : () -> Unit)
    suspend fun createAnonymousAccount(onSuccess : () -> Unit, onFailMessage : () -> Unit)
    suspend fun loginAccount(email: String, password: String, onSuccess : () -> Unit, onFailMessage : () -> Unit)
    suspend fun deleteAccount(onSuccess : () -> Unit, onFailMessage : () -> Unit)
    suspend fun signOut()
}
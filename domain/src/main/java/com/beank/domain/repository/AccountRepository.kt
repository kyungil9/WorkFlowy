package com.beank.domain.repository

interface AccountRepository {
    fun createAccount(email : String, password : String, onSuccess : () -> Unit, onFailMessage : () -> Unit)
    fun createAnonymousAccount(onSuccess : () -> Unit, onFailMessage : () -> Unit)
    fun loginAccount(email: String, password: String, onSuccess : () -> Unit, onFailMessage : () -> Unit)
    fun deleteAccount(onSuccess : () -> Unit, onFailMessage : () -> Unit)
    fun signOut()
}
package com.beank.data.serviceimpl

import androidx.core.os.trace
import com.beank.domain.service.AccountService
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth : FirebaseAuth
) : AccountService{

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override var currentUser: FirebaseUser? = null
        get() = auth.currentUser

    override suspend fun createAccount(email: String, password: String, onSuccess : () -> Unit, onFailMessage : () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                currentUser = auth.currentUser
                onSuccess()
            }else{
                onFailMessage()
            }
        }
    }

    override suspend fun loginAccount(email: String, password: String, onSuccess : () -> Unit, onFailMessage: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                currentUser = auth.currentUser
                onSuccess()
            }else
                onFailMessage()
        }
    }

    override suspend fun createAnonymousAccount(onSuccess : () -> Unit, onFailMessage : () -> Unit) {
        auth.signInAnonymously().addOnCompleteListener { task ->
            if(task.isSuccessful)
                onSuccess()
            else
                onFailMessage()
        }
    }

    override suspend fun deleteAccount(onSuccess : () -> Unit, onFailMessage : () -> Unit) {
        auth.currentUser!!.delete().addOnCompleteListener { task ->
            if (task.isSuccessful)
                onSuccess()
            else
                onFailMessage()
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}
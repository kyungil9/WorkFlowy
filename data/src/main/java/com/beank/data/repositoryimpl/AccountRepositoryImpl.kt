package com.beank.data.repositoryimpl

import com.beank.data.datasource.StorageDataSource
import com.beank.domain.model.Record
import com.beank.domain.model.Tag
import com.beank.domain.repository.AccountRepository
import com.beank.domain.usecase.record.InsertRecord
import com.beank.domain.usecase.tag.InsertTag
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val auth : FirebaseAuth
) : AccountRepository {

    override fun createAccount(email: String, password: String, onSuccess : () -> Unit, onFailMessage : () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                onSuccess()
            }else{
                onFailMessage()
            }
        }
    }

    override fun loginAccount(email: String, password: String, onSuccess : () -> Unit, onFailMessage: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                onSuccess()
            }else
                onFailMessage()
        }
    }

    override fun createAnonymousAccount(onSuccess : () -> Unit, onFailMessage : () -> Unit) {
        auth.signInAnonymously().addOnCompleteListener { task ->
            if(task.isSuccessful)
                onSuccess()
            else
                onFailMessage()
        }
    }

    override fun deleteAccount(onSuccess : () -> Unit, onFailMessage : () -> Unit) {
        auth.currentUser!!.delete().addOnCompleteListener { task ->
            if (task.isSuccessful)
                onSuccess()
            else
                onFailMessage()
        }
    }

    override fun signOut() {
        auth.signOut()
    }
}
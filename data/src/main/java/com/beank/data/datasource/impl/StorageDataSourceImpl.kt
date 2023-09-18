package com.beank.data.datasource.impl

import androidx.core.os.trace
import com.beank.domain.repository.AccountRepository
import com.beank.data.datasource.StorageDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class StorageDataSourceImpl @Inject constructor(
    private val firestore : FirebaseFirestore,
    private val auth: FirebaseAuth
) : StorageDataSource {

    override val store : CollectionReference = firestore.collection(ROOT_COLLECTION)

    override fun getUid(): String? = auth.currentUser?.uid

    override fun <T :Any> save(collectionId: String, document: T) : Unit =
        trace(SAVE_TASK_TRACE){
            store.document(auth.currentUser!!.uid).collection(collectionId).add(document)
        }

    override fun <T : Any> replace(collectionId: String, documentId: String, document: T) : Unit =
        trace(UPDATE_TASK_TRACE){
            store.document(auth.currentUser!!.uid).collection(collectionId).document(documentId).set(document)
        }

    override fun <T : Any> update(collectionId: String, documentId: String, document: Map<String, T>) : Unit =
        trace(UPDATE_TASK_TRACE){
            store.document(auth.currentUser!!.uid).collection(collectionId).document(documentId).update(document)
        }

    override fun delete(collectionId: String, documentId: String) : Unit =
        trace(DELETE_TASK_TRACE){
            store.document(auth.currentUser!!.uid).collection(collectionId).document(documentId).delete()
        }


    companion object {
        private const val ROOT_COLLECTION = "root"
        private const val SAVE_TASK_TRACE = "saveTask"
        private const val UPDATE_TASK_TRACE = "updateTask"
        private const val DELETE_TASK_TRACE = "deleteTask"

    }
}
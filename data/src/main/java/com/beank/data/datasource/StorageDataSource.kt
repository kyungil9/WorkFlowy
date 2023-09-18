package com.beank.data.datasource

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference

interface StorageDataSource {
    val store : CollectionReference
    fun getUid() : String?
    fun <T : Any> save(collectionId: String, document: T)
    fun <T : Any> replace(collectionId: String, documentId: String, document : T)
    fun <T : Any> update(collectionId: String, documentId: String, document : Map<String,T>)
    fun delete(collectionId: String, documentId: String)
}
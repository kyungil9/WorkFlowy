package com.beank.data.utils

import android.annotation.SuppressLint
import com.beank.data.mapper.modelCasting
import com.beank.domain.model.FireStoreState
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.util.Executors
import com.google.firebase.firestore.util.Preconditions
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@SuppressLint("RestrictedApi")
inline fun <reified T : Any, reified E : Any> DocumentReference.dataStateObject(
    metadataChanges: MetadataChanges = MetadataChanges.EXCLUDE
): Flow<FireStoreState<E>> {
    return callbackFlow {
        trySendBlocking(FireStoreState.Loading)
        val registration =
            addSnapshotListener(Executors.BACKGROUND_EXECUTOR, metadataChanges) { snapshot, exception ->
                if (exception != null) {
                    trySendBlocking(FireStoreState.Exception("Error getting DocumentReference snapshot",exception))
                    cancel(message = "Error getting DocumentReference snapshot", cause = exception)
                } else if (snapshot != null) {
                    trySendBlocking(FireStoreState.Success(modelCasting(snapshot.toObject(T::class.java)!!) as E))
                } else {
                    trySendBlocking(FireStoreState.Empty)
                }
            }
        awaitClose { registration.remove() }
    }
}

@SuppressLint("RestrictedApi")
inline fun <reified T : Any, reified E : Any> Query.dataStateObjects(
    metadataChanges: MetadataChanges = MetadataChanges.EXCLUDE
): Flow<FireStoreState<List<E>>> {
    return callbackFlow {
        trySendBlocking(FireStoreState.Loading)
        val registration =
            addSnapshotListener(Executors.BACKGROUND_EXECUTOR, metadataChanges) { snapshot, exception ->
                if (exception != null) {
                    trySendBlocking(FireStoreState.Exception("Error getting DocumentReference snapshot",exception!!))
                    cancel(message = "Error getting DocumentReference snapshot", cause = exception)
                } else if (snapshot != null) {
                    if (snapshot.documents.isEmpty())
                        trySendBlocking(FireStoreState.Empty)
                    else
                        trySendBlocking(FireStoreState.Success(snapshot.toObjects(T::class.java,E::class.java)))
                } else {
                    trySendBlocking(FireStoreState.Empty)
                }
            }
        awaitClose { registration.remove() }
    }
}

@SuppressLint("RestrictedApi")
fun <E, T> QuerySnapshot.toObjects(
    fromType: Class<T>,
    toType: Class<E>,
    serverTimestampBehavior: ServerTimestampBehavior = ServerTimestampBehavior.NONE
): List<E> {
    Preconditions.checkNotNull(fromType, "Provided POJO type must not be null.")
    val res: MutableList<E> = ArrayList()
    for (d in this) {
        res.add(modelCasting(d.toObject(fromType, serverTimestampBehavior)) as E)
    }
    return res
}
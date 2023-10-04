package com.beank.app.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Singleton
    @Provides
    fun auth() = Firebase.auth

    @Singleton
    @Provides
    fun firestore() = Firebase.firestore

    @Singleton
    @Provides
    fun crash() = Firebase.crashlytics

    @Singleton
    @Provides
    fun message() = Firebase.messaging

    @Singleton
    @Provides
    fun storage() = Firebase.storage.reference
}
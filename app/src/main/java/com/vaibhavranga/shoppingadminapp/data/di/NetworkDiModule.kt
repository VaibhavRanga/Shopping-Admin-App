package com.vaibhavranga.shoppingadminapp.data.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vaibhavranga.shoppingadminapp.data.pushNotifications.PushNotifications
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkDiModule {
    @Provides
    @Singleton
    fun provideFirestoreInstance() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    fun providePushNotifications(
        firebaseFirestore: FirebaseFirestore,
        @ApplicationContext context: Context
    ): PushNotifications = PushNotifications(
        firebaseFirestore = firebaseFirestore,
        context = context
    )
}
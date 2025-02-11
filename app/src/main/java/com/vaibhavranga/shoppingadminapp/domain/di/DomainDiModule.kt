package com.vaibhavranga.shoppingadminapp.domain.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vaibhavranga.shoppingadminapp.data.repositoryImpl.RepositoryImpl
import com.vaibhavranga.shoppingadminapp.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainDiModule {
    @Provides
    @Singleton
    fun provideRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): Repository = RepositoryImpl(
        firestore = firestore,
        storage = storage
    )
}
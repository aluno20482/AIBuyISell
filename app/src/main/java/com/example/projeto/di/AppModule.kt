package com.example.projeto.di

import android.app.Application
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestoreDatabase() = Firebase.firestore

    //@Provides
    //fun provideIntroductionSP(
    //    application: Application
   // ) = application.getSharedPreferences(INTRODUCTION_SP, NODE_PRIVATE)

}
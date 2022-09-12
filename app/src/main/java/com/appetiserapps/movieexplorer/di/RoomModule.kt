package com.appetiserapps.movieexplorer.di

import android.content.Context
import androidx.room.Room
import com.appetiserapps.movieexplorer.local.db.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext appContext: Context): Database = Room.databaseBuilder(
        appContext,
        Database::class.java,
        DB_NAME
    ).fallbackToDestructiveMigration()
        .build()

    companion object {
        private const val DB_NAME = "movieexplorer"
    }
}
package id.dev.core.data.di

import android.app.Application
import androidx.room.Room
import id.dev.core.data.local.HistoryDao
import id.dev.core.data.local.HistoryDatabase
import org.koin.dsl.module

fun provideDataBase(application: Application): HistoryDatabase =
    Room.databaseBuilder(
        application,
        HistoryDatabase::class.java,
        "QR_History.db"
    )
        .fallbackToDestructiveMigration(false)
        .build()

fun provideDao(postDataBase: HistoryDatabase): HistoryDao = postDataBase.historyDao

val coreDataModule= module {
    single { provideDataBase(get()) }
    single { provideDao(get()) }
}
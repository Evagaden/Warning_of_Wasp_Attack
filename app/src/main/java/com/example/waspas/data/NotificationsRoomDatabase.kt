package com.example.waspas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NotificationsInfoTable::class], version = 1, exportSchema = false)
abstract class NotificationsRoomDatabase : RoomDatabase(){
    abstract fun notificationInfoDao(): NotificationsInfoDao

    companion object {
        @Volatile
        private var Instance: NotificationsRoomDatabase? = null
        fun getDatabase(context: Context): NotificationsRoomDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, NotificationsRoomDatabase::class.java, "notification_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
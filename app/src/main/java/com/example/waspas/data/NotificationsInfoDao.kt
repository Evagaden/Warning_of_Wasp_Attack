package com.example.waspas.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationsInfoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotificationInfo(notificationInfo: NotificationsInfoTable)

    @Update
    suspend fun updateNotificationInfo(notificationInfo: NotificationsInfoTable)

    @Query("DELETE FROM NotificationsInfo WHERE timestamp < :checkTime")
    suspend fun delete(checkTime: String)

    @Query("SELECT * FROM NotificationsInfo WHERE `order` = :order")
    fun getNotificationById(order: Int): Flow<NotificationsInfoTable>

    @Query("SELECT * FROM NotificationsInfo WHERE deviceID = :deviceId ORDER BY timestamp DESC")
    fun getListNotificationsByDevice(deviceId: String): Flow<List<NotificationsInfoTable>>

    @Query("SELECT * FROM NotificationsInfo ORDER BY timestamp DESC LIMIT 10")
    fun getAllNotificationById(): Flow<List<NotificationsInfoTable>>
}
package com.example.waspas.data

import kotlinx.coroutines.flow.Flow

interface NotificationsInfoRepository {
    suspend fun insertNotificationInfo(notificationInfo: NotificationsInfoTable)

    suspend fun delete(checkTime: String)

    suspend fun update(notificationInfo: NotificationsInfoTable)

    fun getNotificationById(order: Int): Flow<NotificationsInfoTable>

    fun getListNotificationsByDevice(deviceId: String): Flow<List<NotificationsInfoTable>>

    fun getAllNotificationById(): Flow<List<NotificationsInfoTable>>
}
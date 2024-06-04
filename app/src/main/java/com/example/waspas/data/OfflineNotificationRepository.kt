package com.example.waspas.data

import kotlinx.coroutines.flow.Flow

class OfflineNotificationRepository(private val notificationsInfoDao: NotificationsInfoDao) :
    NotificationsInfoRepository {
    override suspend fun insertNotificationInfo(notificationInfo: NotificationsInfoTable) = notificationsInfoDao.insertNotificationInfo(notificationInfo)

    override suspend fun delete(checkTime: String) = notificationsInfoDao.delete(checkTime)

    override suspend fun update(notificationInfo: NotificationsInfoTable) = notificationsInfoDao.updateNotificationInfo(notificationInfo)

    override fun getNotificationById(order: Int): Flow<NotificationsInfoTable> = notificationsInfoDao.getNotificationById(order)

    override fun getListNotificationsByDevice(deviceId: String): Flow<List<NotificationsInfoTable>> =
        notificationsInfoDao.getListNotificationsByDevice(deviceId)
    override fun getAllNotificationById(): Flow<List<NotificationsInfoTable>> = notificationsInfoDao.getAllNotificationById()
}
package com.example.waspas.data

import com.example.waspas.model.CameraInfor
import com.example.waspas.model.FarmInfor
import com.example.waspas.model.User
import com.example.waspas.model.UserInfor
import com.example.waspas.network.AppApiService
import retrofit2.Response

interface AppRepository {
    suspend fun sendLoginInfor(userName: String, password: String, fcm_token: String): Response<UserInfor>
    suspend fun sendSignupInfo(user: User): Response<User>
    suspend fun getFarmByUserId(userId: String): List<FarmInfor>
    suspend fun getCameraByfarmId(farmId: String): List<CameraInfor>
    suspend fun updateAccount(userId: String, user: User): Response<User>
    suspend fun insertFarm(userID: String, farmName: String): Response<FarmInfor>
    suspend fun insertCamera(farmID: String, cameraName: String): Response<CameraInfor>
    suspend fun getCameraInfo(cameraId: String): CameraInfor
    suspend fun getFarmInfo(farmId: String): FarmInfor
}

class NetworkAppRepository(
    private val appApiService: AppApiService
): AppRepository
{
    override suspend fun sendLoginInfor(userName: String, password: String, fcm_token: String): Response<UserInfor> {
        val user = User(password = password, phone = userName, fcm_token = fcm_token)
        return appApiService.sendLoginInfor(user)
    }

    override suspend fun sendSignupInfo(user: User): Response<User> {
        return appApiService.sendSignupInfo(user)
    }

    override suspend fun getFarmByUserId(userId: String): List<FarmInfor> {
        return appApiService.getFarmByUserId(userId)
    }

    override suspend fun getCameraByfarmId(farmId: String): List<CameraInfor> {
        return appApiService.getCameraByFarmId(farmId)
    }

    override suspend fun updateAccount(userId: String, user: User): Response<User> {
        return appApiService.updateAccount(userId, user)
    }

    override suspend fun insertFarm(userID: String, farmName: String): Response<FarmInfor> {
        val farmInfo = FarmInfor(0, "", "", farmName, userID, "", 0)
        return appApiService.insertFarm(farmInfo)
    }

    override suspend fun insertCamera(farmID: String, cameraName: String): Response<CameraInfor> {
        val cameraInfor = CameraInfor(0, "", "", 0, farmID, cameraName, 0,"")
        return appApiService.insertCamera(cameraInfor)
    }

    override suspend fun getCameraInfo(cameraId: String): CameraInfor {
        return appApiService.getCameraInfo(cameraId)
    }

    override suspend fun getFarmInfo(farmId: String): FarmInfor {
        return appApiService.getFarmInfo(farmId)
    }
}
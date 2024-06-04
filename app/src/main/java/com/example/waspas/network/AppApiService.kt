package com.example.waspas.network

import com.example.waspas.model.CameraInfor
import com.example.waspas.model.FarmInfor
import com.example.waspas.model.User
import com.example.waspas.model.UserInfor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AppApiService {
    @POST("signin")
    suspend fun sendLoginInfor(@Body user: User?): Response<UserInfor>

    @POST("signup")
    suspend fun sendSignupInfo(@Body user: User?): Response<User>

    @GET("farm/user/{userId}")
    suspend fun getFarmByUserId(@Path("userId") userId: String): List<FarmInfor>

    @PUT("user/{userId}")
    suspend fun updateAccount(@Path("userId") userId: String, @Body user: User?): Response<User>

    @GET("camdevice/farm/{farmId}")
    suspend fun getCameraByFarmId(@Path("farmId") farmId: String): List<CameraInfor>

    @POST("farm")
    suspend fun insertFarm(@Body farmInfor: FarmInfor): Response<FarmInfor>

    @POST("camdevice")
    suspend fun insertCamera(@Body cameraInfor: CameraInfor): Response<CameraInfor>

    @PUT("camdevice/{cameraId}")
    suspend fun updateCameraInfo(@Path("cameraId") cameraId: String): String

    @GET("camdevice/{cameraId}")
    suspend fun getCameraInfo(@Path("cameraId") cameraId: String): CameraInfor

    @GET("farm/{farmId}")
    suspend fun getFarmInfo(@Path("farmId") farmId: String): FarmInfor
}
package com.example.waspas.data

import android.content.Context
import com.example.waspas.network.AppApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

interface AppContainer  {
    val appRepository: AppRepository
    val notificationsInfoRepository: NotificationsInfoRepository
}

class DefaultAppContainer(private val context: Context): AppContainer
{
    private val baseUrl =
        "http://103.176.178.96:8000/api/v1/"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val retrofit = Retrofit.Builder()
        //.addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    /**
     * A public Api object that exposes the lazy-initialized Retrofit service
     */

    private val retrofitService: AppApiService by lazy {
        retrofit.create(AppApiService::class.java)
    }

    override val appRepository: AppRepository by lazy{
        NetworkAppRepository(retrofitService)
    }

    override val notificationsInfoRepository: NotificationsInfoRepository by lazy {
        OfflineNotificationRepository(NotificationsRoomDatabase.getDatabase(context).notificationInfoDao())
    }
}
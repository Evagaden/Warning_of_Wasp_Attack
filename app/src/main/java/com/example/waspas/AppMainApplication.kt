package com.example.waspas

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.waspas.data.AppContainer
import com.example.waspas.data.DefaultAppContainer
import com.example.waspas.data.UserDataStore

private const val USER_PREFERENCE_DATA = "userPreferenceData"
private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCE_DATA
)


class AppMainApplication(): Application() {
    lateinit var container: AppContainer
    lateinit var dataStore: UserDataStore
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        dataStore = UserDataStore(userDataStore)
    }
}
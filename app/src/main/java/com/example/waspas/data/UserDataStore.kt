package com.example.waspas.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class UserDataStore(
    private val userDataStore: DataStore<Preferences>
) {
    private companion object {
        val User_Name = stringPreferencesKey("user_name")
        val Password = stringPreferencesKey("password")
    }

    val userName: Flow<String> = userDataStore.data
        .catch {
            if (it is IOException) {
                Log.e("UserDataStore", "Error reading preferences.", it)
            } else {
                throw it
            }
        }
        .map { preferences ->
            Log.d("UserDataStore", preferences[User_Name]?:"")
            preferences[User_Name] ?: ""
        }

    val password: Flow<String> = userDataStore.data
        .catch {
            if (it is IOException) {
                Log.e("UserDataStore", "Error reading preferences.", it)
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[Password] ?:""
        }

    suspend fun saveUserNamePassword(userName: String, password: String) {
        userDataStore.edit { preferences ->
            preferences[User_Name] = userName
            preferences[Password] = password
        }
    }
}
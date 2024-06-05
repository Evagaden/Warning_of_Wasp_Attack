package com.example.waspas.ui.state

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.waspas.AppMainApplication
import com.example.waspas.R
import com.example.waspas.data.AppRepository
import com.example.waspas.data.NotificationsInfoRepository
import com.example.waspas.data.NotificationsInfoTable
import com.example.waspas.data.UserDataStore
import com.example.waspas.model.BeeInfo
import com.example.waspas.model.CameraInfor
import com.example.waspas.model.FarmInfor
import com.example.waspas.model.NotificationInfo
import com.example.waspas.model.User
import com.example.waspas.model.UserInfor
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import java.io.IOException

sealed interface UiState {
    object Success : UiState
    object Error : UiState
    object Loading :UiState
}

data class ListNotifications(val listNofications: List<NotificationsInfoTable> = listOf())
class AppViewModel(private val appRepository: AppRepository,
                   private val notificationsInfoRepository: NotificationsInfoRepository,
                   private val userDataStore: UserDataStore
): ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    val notificationListInfo: StateFlow<ListNotifications> =
        notificationsInfoRepository.getAllNotificationById().map {
            Log.d("AppViewModel", "a")
            ListNotifications(it)
        }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(100L),
        initialValue = ListNotifications()
    )

    init {
        checkFirstUserNamePassword()
    }

    private fun checkFirstUserNamePassword()
    {
        runBlocking {
            if(userDataStore.userName.first() != "")
            {
                runBlocking {
                    val token = Firebase.messaging.token.await()
                    Log.d("AppViewModel", token)
                    val result = appRepository.sendLoginInfor(
                        userDataStore.userName.first(),
                        userDataStore.password.first(), token)
                    val user = result.body()?.user?:uiState.value.user
                    if (user.name != "") {
                        _uiState.update { currentState ->
                            currentState.copy(
                                user = User(
                                    _id = user._id,
                                    email = user.email,
                                    name = user.name,
                                    password = userDataStore.password.first(),
                                    phone = userDataStore.userName.first(),
                                    role = user.role
                                ),
                                loginState = UiState.Success
                            )
                        }
                        getListFarm()
                    }
                }
            }
        }
    }

    fun setCheckedNotificationInfoTable(it: NotificationsInfoTable)
    {
        val a = NotificationsInfoTable(it.order, true, it.beeDensity, it.beeInfo, it.deviceID, it.farmID, it.timestamp)
        viewModelScope.launch {
            notificationsInfoRepository.update(a)
        }
    }

    fun checkUserNamePassword(context: Context, userName: String, password: String)
    {
        runBlocking{
            try {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("AppViewModel", "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    //val token = task.result
                })
                val token = Firebase.messaging.token.await()
                Log.d("AppViewModel", token)
                val result = appRepository.sendLoginInfor(userName, password, token)
                val user = result.body()?.user?:uiState.value.user
                if(user.name != "")
                {
                    _uiState.update { currentState ->
                        currentState.copy(
                            user = User(_id = user._id, email = user.email, name = user.name, password = password, phone = userName, role = user.role),
                            loginState = UiState.Success)
                    }
                    getListFarm()
                    saveLoginInfo(userName, password)
                }
                else
                {
                    var error = Gson().fromJson(result.errorBody()?.string(), UserInfor::class.java).error
                    Log.d("AppViewModelE", error)
                    if(error.equals("phone number and password dont match"))
                    {
                        _uiState.update { currentState ->
                            currentState.copy(
                                loginState = UiState.Error)
                        }
                        updateLoginInfo("", "")
                        saveLoginInfo("", "")

                        Toast.makeText(context, getString(context, R.string.invalid_user_name_password), Toast.LENGTH_LONG).show()
                    }else if (error.equals("User with that phone number does not exist. Please signup"))
                    {
                        _uiState.update { currentState ->
                            currentState.copy(
                                loginState = UiState.Error)
                        }
                        updateLoginInfo("", "")
                        saveLoginInfo("", "")
                        Toast.makeText(context, getString(context, R.string.user_doesnt_exit), Toast.LENGTH_LONG).show()
                    }
                }
            } catch(e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        loginState = UiState.Error)
                }
                updateLoginInfo("", "")
                Log.d("AppViewModele", e.message.toString())
                Toast.makeText(context, getString(context, R.string.network_error), Toast.LENGTH_LONG).show()
            }
        }
    }


    fun getListFarm()
    {
        viewModelScope.launch {
            try {
                val listFarm = appRepository.getFarmByUserId(uiState.value.user._id)
                _uiState.update { currentState ->
                    currentState.copy(farmState = UiState.Success, listFarm = listFarm)
                }
            } catch (e: IOException) {
                _uiState.update { currentState ->
                    currentState.copy(farmState = UiState.Error)
                }
            } catch (e: HttpException) {
                _uiState.update { currentState ->
                    currentState.copy(farmState = UiState.Error)
                }
            }
        }
    }

    fun getListCamera(farmId: String)
    {
        viewModelScope.launch {
            try {
                val listCamera = appRepository.getCameraByfarmId(farmId)
                _uiState.update { currentState ->
                    currentState.copy(cameraState = UiState.Success, listCamera = listCamera)
                }
            } catch (e: IOException) {
                _uiState.update { currentState ->
                    currentState.copy(cameraState = UiState.Error)
                }
            } catch (e: HttpException) {
                _uiState.update { currentState ->
                    currentState.copy(cameraState = UiState.Error)
                }
            }
        }
    }

    fun getCameraInfo(cameraID: String): CameraInfor
    {
        return runBlocking {
            appRepository.getCameraInfo(cameraID)
        }
    }

    fun getFarmInfo(farmId: String): FarmInfor
    {
        return runBlocking {
            appRepository.getFarmInfo(farmId)
        }
    }

    fun updateFarmInfo(farmInfo: FarmInfor)
    {
        _uiState.update { currentState ->
            currentState.copy(farm = farmInfo)
        }
    }

    fun updateLoginInfo(userName: String, password: String)
    {
        _uiState.update { currentState ->
            currentState.copy(loginInfo = listOf(userName, password))
        }
    }

    fun updateNotificationInfo(notificationInfoTable: NotificationsInfoTable)
    {
        _uiState.update { currentState ->
            currentState.copy(notificationInfo = notificationInfoTable)
        }
    }
    fun sendSignupInfo(
        check: Boolean,
        context: Context,
        message: String,
        messageCheck: String,
        user: User
    ) {
        if(check)
        {
            runBlocking{
                try {
                    val result = appRepository.sendSignupInfo(user).body()
                    if(result?._id == null)
                    {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        _uiState.update { currentState ->
                            currentState.copy(signupState = UiState.Error)
                        }
                    }
                    else
                    {
                        _uiState.update { currentState ->
                            currentState.copy(
                                user = result,
                                signupState = UiState.Success
                            )
                        }
                        getListFarm()
                    }
                } catch(e: Exception) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    _uiState.update { currentState ->
                        currentState.copy(signupState = UiState.Error)
                    }
                }
            }
        }
        else
        {
            Toast.makeText(context, messageCheck, Toast.LENGTH_LONG).show()
        }
    }

    suspend fun saveLoginInfo(userName: String, password: String)
    {
        userDataStore.saveUserNamePassword(userName,password)
    }

    fun logOut()
    {
        runBlocking {
            userDataStore.saveUserNamePassword("", "")
            _uiState.update { currentState ->
                currentState.copy(
                    loginState = UiState.Loading,
                    loginInfo = listOf("", "")
                )
            }
        }
    }

    fun insertDevice(
        farmID: String,
        cameraName: String,
        context: Context,
        message: String,
        messageCheck: String,
        messageWarning: String)
    {
        if(cameraName == ""){
            Toast.makeText(context, messageWarning, Toast.LENGTH_LONG).show()
        }
        else
        {
            runBlocking {
                try {
                    val result = appRepository.insertFarm(uiState.value.user._id, cameraName).body()
                    if(result != null)
                    {
                        getListFarm()
                        Toast.makeText(context, messageCheck, Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()

                    }
                }
                catch (e: Exception)
                {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun insertFarm(
        farmName: String,
        context: Context,
        message: String,
        messageCheck: String,
        messageWarning: String,
    ) {
        if(farmName == ""){
            Toast.makeText(context, messageWarning, Toast.LENGTH_LONG).show()
        }
        else
        {
            runBlocking {
                try {
                    val result = appRepository.insertFarm(uiState.value.user._id, farmName).body()
                    Log.d("AppViewModel", result?.ownerID?:"aaaaa")
                    if(result != null)
                    {
                        getListFarm()
                        Toast.makeText(context, messageCheck, Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()

                    }
                }
                catch (e: Exception)
                {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun updateUserInfo(
        context: Context,
        message: String,
        messageCheck: String,
        messageWarning: String,
        user: User)
    {
        if(user.password == "" || user.email == "" || user.name == ""){
            Toast.makeText(context, messageWarning, Toast.LENGTH_LONG).show()
        }
        else
        {
            runBlocking {
                try {
                    val result = appRepository.updateAccount(uiState.value.user._id, user).body()
                    if(result != null)
                    {
                        _uiState.update { currentState ->
                            currentState.copy(user = result)
                        }
                        saveLoginInfo(result.phone, result.password)
                        Toast.makeText(context, messageCheck, Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()

                    }
                }
                catch (e: Exception)
                {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun changeNotificationsInfoTableToNotificationInfo(notificationsInfoTable: NotificationsInfoTable): NotificationInfo
    {
        return NotificationInfo(
            beeDensity  = notificationsInfoTable.beeDensity,
            beeInfo = Gson().fromJson(notificationsInfoTable.beeInfo, object : TypeToken<List<BeeInfo>>() {}.type),
            deviceID = notificationsInfoTable.deviceID,
            farmID = notificationsInfoTable.farmID,
            timestamp = notificationsInfoTable.timestamp,
        )
    }

    fun changeNotificationInfoToNotificationsInfoTable(notificationInfo: NotificationInfo): NotificationsInfoTable
    {
        return NotificationsInfoTable(
        beeDensity = notificationInfo.beeDensity,
        beeInfo = Gson().toJson(notificationInfo.beeInfo),
        deviceID = notificationInfo.deviceID,
        farmID = notificationInfo.farmID,
        timestamp = notificationInfo.timestamp
    )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AppMainApplication)
                val appRepository = application.container.appRepository
                val notificationsInfoRepository = application.container.notificationsInfoRepository
                AppViewModel(
                    appRepository = appRepository,
                    notificationsInfoRepository = notificationsInfoRepository,
                    userDataStore = application.dataStore
                )
            }
        }
    }
}
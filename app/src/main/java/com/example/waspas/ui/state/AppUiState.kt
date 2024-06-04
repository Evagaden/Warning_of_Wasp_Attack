package com.example.waspas.ui.state

import com.example.waspas.data.NotificationsInfoTable
import com.example.waspas.model.CameraInfor
import com.example.waspas.model.FarmInfor
import com.example.waspas.model.User

data class AppUiState(
    val loginState: UiState = UiState.Loading,
    val signupState: UiState = UiState.Loading,
    val farmState: UiState = UiState.Loading,
    val cameraState: UiState = UiState.Loading,
    val loginInfo: List<String> = listOf("", ""),
    val user: User = User("","","","",0, "", ""),
    val listFarm: List<FarmInfor> = arrayListOf(),
    val farm: FarmInfor = FarmInfor(0, "", "", "", "", "", 0),
    val listCamera: List<CameraInfor> = arrayListOf(),
    val notificationInfo: NotificationsInfoTable = NotificationsInfoTable(),
    val notificationListInfo: List<NotificationsInfoTable> = arrayListOf(),
    val error: String = ""
)

package com.example.waspas

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.waspas.model.NotificationInfo
import com.example.waspas.ui.FarmScreen
import com.example.waspas.ui.LoginScreen
import com.example.waspas.ui.NotificationInfoScreen
import com.example.waspas.ui.QrScannerScreen
import com.example.waspas.ui.SignUpScreen
import com.example.waspas.ui.UserScreen
import com.example.waspas.ui.state.AppViewModel
import com.google.gson.Gson

enum class AppScreen()
{
    LoginScreen, SignUpScreen, UserScreen,
    NotificationInforScreen,
    FarmScreen, QrScannerScreen
}
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun App(
    notification: String,
    timestamp: String,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModel = viewModel(factory = AppViewModel.Factory)
) {
    Scaffold { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        val notificationListInfo by viewModel.notificationListInfo.collectAsState()
        Log.d("AppScreen", "listChange")
        if(!notification.equals("")){
            val tempNoti = Gson().fromJson(notification, NotificationInfo::class.java)
            val farmID = viewModel.getCameraInfo(tempNoti.deviceID).farmID
            val notificationInfo = NotificationInfo(tempNoti.beeDensity, tempNoti.beeInfo, tempNoti.deviceID, farmID, timestamp)
            viewModel.updateNotificationInfo(viewModel.changeNotificationInfoToNotificationsInfoTable(notificationInfo))
        }
        else
        {
            NotificationInfo()
        }
        NavHost(
            navController = navController,
            startDestination = if(uiState.notificationInfo.deviceID.equals(""))
            {AppScreen.LoginScreen.name} else {AppScreen.NotificationInforScreen.name},
            modifier = Modifier
                    .padding(innerPadding)
        ) {
            composable(
                route = AppScreen.LoginScreen.name,
                content = {
                    LoginScreen(
                        modifier = Modifier
                            .fillMaxSize(),
                        error = uiState.error,
                        loginState = uiState.loginState,
                        loginInfo = uiState.loginInfo,
                        navController = navController,
                        updateLoginInfo = { userName, password -> viewModel.updateLoginInfo(userName, password)},
                        checkUserNamePassword = { context, userName, password ->
                            viewModel.checkUserNamePassword(context, userName, password)
                        }
                    ) { navController.navigate(AppScreen.SignUpScreen.name) }
                }
            )

            composable(
                route = AppScreen.SignUpScreen.name,
            )
            {
                SignUpScreen(
                    signupState = uiState.signupState,
                    navController = navController,
                    onSignUpButtonClicked = {check, context, message, messageCheck, user ->
                        viewModel.sendSignupInfo(check, context, message, messageCheck, user)}
                )
            }

            composable(
                route = AppScreen.UserScreen.name
            ){
                UserScreen(
                    modifier = modifier
                        .padding(8.dp),
                    farmState = uiState.farmState,
                    user = uiState.user,
                    listFarm = uiState.listFarm,
                    onFarmButtonClicked = {
                        farmInfo ->
                        viewModel.updateFarmInfo(farmInfo)
                        viewModel.getListCamera(uiState.farm._id)
                        navController.navigate(AppScreen.FarmScreen.name)
                    },
                    onAddButtonClicked = {farmName,context, message, messageCheck, messageWarning -> viewModel.insertFarm(farmName, context, message, messageCheck, messageWarning)},
                    onUpdateInfoButtonClicked = {context, message, messageCheck, messageWarning, user ->
                                                viewModel.updateUserInfo(context, message, messageCheck, messageWarning, user)
                    },
                    onLogoutButtonCLicked = { viewModel.logOut()
                    navController.navigate(AppScreen.LoginScreen.name)},
                    onDeleteButtonClicked = {id -> {}},
                    onNotificationInfoButtonClicked = {notificationsInfoTable ->
                        viewModel.updateNotificationInfo(notificationsInfoTable)
                        navController.navigate(AppScreen.NotificationInforScreen.name)
                        viewModel.setCheckedNotificationInfoTable(notificationsInfoTable)
                    },
                    listNotification = notificationListInfo.listNofications
                )
            }

            composable(route = AppScreen.FarmScreen.name)
            {
                FarmScreen(
                    modifier = Modifier
                        .padding(8.dp),
                    cameraState = uiState.cameraState,
                    userName = uiState.user.name,
                    farmName = uiState.farm.name,
                    listCamera = uiState.listCamera,
                    onBackButtonClicked = {navController.navigateUp()},
                    onCameraButtonClicked = {/*navController.navigate(AppScreen.NotificationInforScreen.name)*/},
                    onAddButtonClicked = {navController.navigate(AppScreen.QrScannerScreen.name)}
                )
            }

            composable(route = AppScreen.QrScannerScreen.name)
            {
                QrScannerScreen(onQrCodeScanned = {/*farmID, context, message, messageCheck -> viewModel.insertDevice(
                    deviceId)*/})
            }

            composable(route = AppScreen.NotificationInforScreen.name)
            {
                NotificationInfoScreen(
                    notificationInfoTable = uiState.notificationInfo,
                    onBackButtonClicked = {navController.navigate(AppScreen.UserScreen.name)}
                )
            }
        }
    }
}
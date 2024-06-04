package com.example.waspas.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.WarningOfWaspAttackTheme
import com.example.waspas.R
import com.example.waspas.data.NotificationsInfoTable
import com.example.waspas.ui.state.AppViewModel

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    onNotificationButtonClicked:(NotificationsInfoTable)->Unit,
    listNotification: List<NotificationsInfoTable>
){
    ElevatedCard(
        modifier = modifier
            .aspectRatio(0.5F)
            .fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        LazyColumn{
            items(listNotification){
                NotificationButton(
                    notificationsInfoTable = it,
                    onNotificationButtonClicked = onNotificationButtonClicked
                )
            }
       }
    }
}

@Composable
fun NotificationButton(
    modifier: Modifier = Modifier,
    notificationsInfoTable: NotificationsInfoTable,
    onNotificationButtonClicked: (NotificationsInfoTable) -> Unit,
    viewModel: AppViewModel = viewModel(factory = AppViewModel.Factory)
){
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
            ,
        shape = RoundedCornerShape(5),
        onClick = { onNotificationButtonClicked(notificationsInfoTable)},
        colors = ButtonDefaults.buttonColors(
            containerColor = if(notificationsInfoTable.check){
                Log.d("NotificationScree", "co mau trang")
                MaterialTheme.colorScheme.onPrimary
            }else
            {
                Log.d("NotificationScree", "co mau do")
                MaterialTheme.colorScheme.onErrorContainer
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ){
            val notificationInfo = viewModel.changeNotificationsInfoTableToNotificationInfo(notificationsInfoTable)
            val farmName = viewModel.getFarmInfo(notificationInfo.farmID).name
            val cameraName = viewModel.getCameraInfo(notificationInfo.deviceID).name

            Text(
                fontSize = 20.sp,
                text = stringResource(id = R.string.notification_title, notificationsInfoTable.beeDensity.toString()),
                color = MaterialTheme.colorScheme.scrim
            )
            Text(
                fontSize = 20.sp,
                text = stringResource(id = R.string.message_notification3, notificationsInfoTable.timestamp),
                color = MaterialTheme.colorScheme.scrim
            )
            Text(
                fontSize = 20.sp,
                text = stringResource(id = R.string.message_notification1,  farmName),
                color = MaterialTheme.colorScheme.scrim
            )
            Text(
                fontSize = 20.sp,
                text = stringResource(id = R.string.message_notification2, cameraName),
                color = MaterialTheme.colorScheme.scrim
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview(modifier: Modifier = Modifier)
{
    WarningOfWaspAttackTheme {
        NotificationsScreen(
            onNotificationButtonClicked = {},
            listNotification = arrayListOf(
                NotificationsInfoTable(1, false, 2.6, "", "123", "vuon1", "12:00"),
                NotificationsInfoTable(1, false, 2.6, "", "123", "vuon1", "12:00"),
                NotificationsInfoTable(1, false, 2.6, "", "123", "vuon1", "12:00"),
            ))
    }
}
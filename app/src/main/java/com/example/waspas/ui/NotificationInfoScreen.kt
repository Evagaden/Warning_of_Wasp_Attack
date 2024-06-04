package com.example.waspas.ui

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.compose.WarningOfWaspAttackTheme
import com.example.waspas.R
import com.example.waspas.data.NotificationsInfoTable
import com.example.waspas.model.NotificationInfo
import com.example.waspas.ui.state.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationInfoScreen(
    notificationInfoTable: NotificationsInfoTable,
    viewModel: AppViewModel = viewModel(factory = AppViewModel.Factory),
    onBackButtonClicked: ()->Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                ),
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackButtonClicked) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
        ) {
            val notificationInfo = viewModel.changeNotificationsInfoTableToNotificationInfo(notificationInfoTable)
            Log.d("NotificationInfoScreen", notificationInfoTable.beeInfo)
            val farmName = viewModel.getFarmInfo(notificationInfo.farmID).name
            val cameraName = viewModel.getCameraInfo(notificationInfo.deviceID).name

            Text(
                text = stringResource(id = R.string.message_notification0),
                modifier = Modifier
                    .padding(4.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.scrim
            )
            Text(
                text = stringResource(id = R.string.message_notification1, farmName),
                modifier = Modifier
                    .padding(4.dp),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.scrim

            )
            Text(
                text = stringResource(id = R.string.message_notification2, cameraName),
                modifier = Modifier
                    .padding(4.dp),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.scrim
            )
            Text(
                text = stringResource(id = R.string.message_notification3, notificationInfo.timestamp),
                modifier = Modifier
                    .padding(4.dp),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.scrim
            )
            ListPhotosCard(notificationInfo = notificationInfo)
        }
    }
}

@Composable
fun PhotoCard(image: String, modifier: Modifier = Modifier, boxes: List<List<Double>>)
{
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    )
    {
        val link = "http://103.176.178.96:8000/api/v1/photo/$image"
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(link)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(id = R.string.image),
            placeholder = painterResource(id = R.drawable.loading_images),
            error = painterResource(id = R.drawable.erro_connection_img),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(8f / 6f)
                .drawWithCache{
                    val listPath = mutableListOf<Path>()
                    for(box in boxes)
                    {
                        val scale = 800/size.width
                        val x1 = (box[0]/scale).toFloat()
                        val y1 = (box[1]/scale).toFloat()
                        val x2 = (box[2]/scale).toFloat()
                        val y2 = (box[3]/scale).toFloat()
                        val path = Path()
                        path.moveTo(x1, y1)
                        path.lineTo(x2, y1)
                        path.lineTo(x2, y2)
                        path.lineTo(x1, y2)
                        path.close()
                        listPath.add(path)
                    }
                    onDrawWithContent {
                        drawContent()
                        for(path in listPath)
                        {
                            drawPath(path, Color.Red, style = Stroke(width = 3f))
                        }
                    }
                }
        )
    }
}

@Composable
fun ListPhotosCard(notificationInfo: NotificationInfo, modifier: Modifier = Modifier)
{
    LazyVerticalGrid (
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier
            .padding(8.dp),
        contentPadding = PaddingValues(6.dp),
    ){
        items(notificationInfo.beeInfo)
        {
                beeInfo -> PhotoCard(
            image = beeInfo.imageID,
            boxes = beeInfo.boxes,
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth()
                .aspectRatio(8f / 6f)
                )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestNotificationInforScreen()
{
    WarningOfWaspAttackTheme {
       PhotoCard("65cecf656129446124b11c95", Modifier, arrayListOf())
    }
}
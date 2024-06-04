package com.example.waspas.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import com.example.waspas.R
import com.example.waspas.model.CameraInfor
import com.example.waspas.ui.state.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmScreen(
    modifier: Modifier = Modifier,
    cameraState: UiState,
    userName: String = "",
    farmName: String = "",
    listCamera: List<CameraInfor> = arrayListOf(),
    onBackButtonClicked: () -> Unit = {},
    onNotificationButtonClicked: () -> Unit = {},
    onCameraButtonClicked: (String) -> Unit = {},
    onAddButtonClicked: () -> Unit = {},
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
                title = {
                    Text(
                        text = userName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackButtonClicked) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNotificationButtonClicked) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_notifications_24),
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->

        when(cameraState)
        {
            is UiState.Loading -> {
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = painterResource(id = R.drawable.loading_images),
                    contentDescription = "State is loading"
                )
            }
            is UiState.Success -> {
                Box{
                    LazyColumn(
                        contentPadding = innerPadding
                    ) {
                        items(listCamera) {
                            CameraButton(
                                modifier = Modifier,
                                cameraName = it.name,
                                cameraId = it._id,
                                onCameraButtonClicked = onCameraButtonClicked
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_medium))
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        SmallFloatingActionButton(
                            modifier = Modifier.size(dimensionResource(id = R.dimen.image_size)),
                            onClick = { /*TODO*/ },
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.secondary,
                            shape = CircleShape,

                            ) {
                            Icon(Icons.Filled.Add, "Small floating action button.")
                        }
                    }
                }
            }
            is UiState.Error -> {
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = painterResource(id = R.drawable.erro_connection_img),
                    contentDescription = "State is Error"
                )

                Text(
                    text = stringResource(id = R.string.erro),
                    color = MaterialTheme.colorScheme.primary
                )

                ClickableText(
                    text = AnnotatedString(stringResource(id = R.string.try_again)),
                    onClick = {/*TODO*/},
                    style = TextStyle(color = Color.Blue, fontStyle = FontStyle.Italic)
                )
            }
        }
    }
}



@Composable
fun CameraButton(
    modifier: Modifier,
    cameraName: String,
    cameraId: String,
    onCameraButtonClicked: (String) -> Unit,
) {
    Card(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
        shape = RoundedCornerShape(25)
    ) {
        OutlinedButton(
            onClick = { onCameraButtonClicked(cameraId) },
            shape = RoundedCornerShape(25)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                Image(
                    modifier = modifier
                        .size(dimensionResource(R.dimen.image_size)),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(R.drawable.camera_removebg_preview),
                    contentDescription = null
                )

                Text(
                    text = cameraName,
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = { /*TODO*/ },
                ) {
                    Icon(
                        painterResource(id = R.drawable.baseline_more_vert_24),
                        contentDescription = "setting button",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}



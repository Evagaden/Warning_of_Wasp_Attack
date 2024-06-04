package com.example.waspas.ui

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.WarningOfWaspAttackTheme
import com.example.waspas.R
import com.example.waspas.data.NotificationsInfoTable
import com.example.waspas.model.FarmInfor
import com.example.waspas.model.User
import com.example.waspas.ui.state.UiState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    farmState: UiState,
    user: User,
    listFarm: List<FarmInfor> = arrayListOf(),
    onFarmButtonClicked: (FarmInfor) -> Unit,
    onAddButtonClicked: (String, Context, String, String, String,) -> Unit,
    onDeleteButtonClicked: (String) -> Unit,
    onUpdateInfoButtonClicked: (Context, String, String, String, User) -> Unit,
    onLogoutButtonCLicked: () -> Unit,
    onNotificationInfoButtonClicked: (NotificationsInfoTable) -> Unit,
    listNotification: List<NotificationsInfoTable>
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var visible by remember {
        mutableStateOf(false)
    }
    var visibleAdd by remember{
        mutableStateOf(false)
    }

    var visibleList by remember{
        mutableStateOf(false)
    }

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
                        text = user.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            visible = !visible
                            visibleAdd = false
                            visibleList = false
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_manage_accounts_24),
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            visibleList = !visibleList
                            visibleAdd = false
                            visible = false
                        }) {
                        BadgedBox(
                            badge = {
                                Badge {
                                    var badgeNumber = 0
                                    listNotification.forEach{
                                        if(!it.check)
                                        {
                                            badgeNumber += 1
                                        }
                                    }

                                    Text(
                                        badgeNumber.toString()
                                    )
                                }
                            }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_notifications_24),
                                contentDescription = "Localized description"
                            )
                        }
                        }
                        },
                        scrollBehavior = scrollBehavior,
                    )
        },
    ) { innerPadding ->
        when(farmState)
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
                Log.d("UserScreen", "run")
                Box{
                    LazyColumn(
                        contentPadding = innerPadding
                    ) {
                        items(listFarm) {
                            farm ->
                            SwipeToDeleteContainer(
                                item = farm,
                                farmId = farm._id,
                                onDelete = onDeleteButtonClicked
                            ) {
                                farm ->
                                FarmButton(
                                    modifier = Modifier,
                                    farmName = farm.name,
                                    farmInfo = farm,
                                    onFarmButtonClicked = onFarmButtonClicked
                                )
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInHorizontally(animationSpec = tween(durationMillis = 200)) { fullWidth ->
                            // Offsets the content by 1/3 of its width to the left, and slide towards right
                            // Overwrites the default animation with tween for this slide animation.
                            -fullWidth / 3
                        } + fadeIn(
                            // Overwrites the default animation with tween
                            animationSpec = tween(durationMillis = 200)
                        ),
                        exit = slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessHigh)) {
                            // Overwrites the ending position of the slide-out to 200 (pixels) to the right
                            -200
                        } + fadeOut()
                    ) {
                        UserInfoScreen(
                            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                            user = user,
                            onLogoutButtonCLicked = onLogoutButtonCLicked,
                            onUpdateInfoButtonClicked = onUpdateInfoButtonClicked)
                    }

                    AnimatedVisibility(
                        visible = visibleList,
                        enter = slideInHorizontally(animationSpec = tween(durationMillis = 200)) { fullWidth ->
                            // Offsets the content by 1/3 of its width to the left, and slide towards right
                            // Overwrites the default animation with tween for this slide animation.
                            fullWidth / 3
                        } + fadeIn(
                            // Overwrites the default animation with tween
                            animationSpec = tween(durationMillis = 200)
                        ),
                        exit = slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessHigh)) {
                            // Overwrites the ending position of the slide-out to 200 (pixels) to the right
                            -200
                        } + fadeOut()
                    ) {
                        NotificationsScreen(
                            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                            onNotificationButtonClicked = onNotificationInfoButtonClicked,
                            listNotification = listNotification)
                    }

                    if(visibleAdd)
                    {
                        AddFarm(onClearButtonClicked = {visibleAdd = false}, onAddButtonClicked = onAddButtonClicked)
                    }

                    AddButton {
                        visibleAdd = true
                        visible = false
                        visibleList = false
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
fun AddFarm(
    onClearButtonClicked: ()->Unit,
    onAddButtonClicked: (String, Context, String, String, String,) -> Unit
)
{
    val context = LocalContext.current
    val message = stringResource(id = R.string.update_account_fail)
    val messageCheck = stringResource(id = R.string.update_account_success)
    val messageWarning = stringResource(id = R.string.update_account_warning)
    var farmName by remember {
        mutableStateOf("")
    }
    ElevatedCard(
        modifier = Modifier.padding(top = 100.dp, start = 25.dp, end = 25.dp),
        shape = RoundedCornerShape(10),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            Row {
                Spacer(modifier = Modifier.weight(1F))
                IconButton(onClick = onClearButtonClicked) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_clear_24),
                        contentDescription = "clear"
                    )
                }
            }
            Text(text = stringResource(id = R.string.import_farm))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = farmName,
                onValueChange = { farmName = it },
                singleLine = true
            )
            Button(
                modifier = Modifier
                    .padding(30.dp)
                    .align(Alignment.End),
                onClick = {
                    onAddButtonClicked(
                        farmName,
                        context,
                        message,
                        messageCheck,
                        messageWarning
                    )
                },
                shape = RoundedCornerShape(25)
            ) {
                Text(
                    text = stringResource(id = R.string.add),
                    style = TextStyle(fontSize = 30.sp)
                )
            }
        }
    }
}

@Composable
fun AddButton(
    onAddButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .fillMaxSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        SmallFloatingActionButton(
            modifier = Modifier.size(dimensionResource(id = R.dimen.image_size)),
            onClick = onAddButtonClicked,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary,
            shape = CircleShape,

            ) {
            Icon(Icons.Filled.Add, "Small floating action button.")
        }
    }
}

@Composable
fun FarmButton(
    modifier: Modifier,
    farmName: String,
    farmInfo: FarmInfor,
    onFarmButtonClicked: (FarmInfor) -> Unit,
    ) {
    Card(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
        shape = RoundedCornerShape(25)
    ) {
        OutlinedButton(
            onClick = { onFarmButtonClicked(farmInfo) },
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
                    painter = painterResource(R.drawable.farm_removebg_preview),
                    contentDescription = null
                )

                Text(
                    text = farmName,
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    farmId: String,
    onDelete: (String) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberDismissState(
        confirmValueChange = { value ->
            if (value == DismissValue.DismissedToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if(isRemoved) {
            delay(animationDuration.toLong())
            onDelete(farmId)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = state,
            background = {
                DeleteBackground(swipeDismissState = state)
            },
            dismissContent = { content(item) },
            directions = setOf(DismissDirection.EndToStart)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: DismissState
) {
    val color = if (swipeDismissState.dismissDirection == DismissDirection.EndToStart) {
        Color.Red
    } else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WaspAppPreview(modifier: Modifier = Modifier)
{
    WarningOfWaspAttackTheme {
        AddFarm(onClearButtonClicked = { /*TODO*/ }, onAddButtonClicked = {a,b,c,d,e -> })
    }
}



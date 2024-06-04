package com.example.waspas.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.waspas.AppScreen
import com.example.waspas.R
import com.example.waspas.ui.state.UiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    error: String,
    loginState: UiState,
    loginInfo: List<String>,
    navController: NavController,
    updateLoginInfo: (String, String) -> Unit,
    checkUserNamePassword: (Context, String, String) -> Unit,
    onClickableTextClicked: (Int) -> Unit
) {
    val context = LocalContext.current
    var isWrong by remember { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember {FocusRequester()}
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
            .imeNestedScroll(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        val imeInsert = WindowInsets.ime.getBottom(LocalDensity.current)
        if(imeInsert == 0)
        {
            Image(
                painter = painterResource(id = R.drawable.bee_removebg_preview),
                contentDescription = null
            )
        }

        when(loginState)
        {
            is UiState.Loading -> {
                Log.d("LoginScreen", "a")
                Text(
                    text = stringResource(id = R.string.greeting),
                    color = Color.Blue,
                )
            }
            is UiState.Success -> {
                Log.d("LoginScreen", "b")
                navController.navigate(AppScreen.UserScreen.name){
                    popUpTo(AppScreen.LoginScreen.name){
                        inclusive = true
                    }
                }
            }
            is UiState.Error -> {
                Log.d("LoginScreen", "c")
                isWrong = true
                Text(
                    text = stringResource(id = R.string.invalid_user_name_password),
                    color = Color.Red,
                    fontStyle = FontStyle.Italic
                )
            }
        }


        OutlinedTextField(
            value = loginInfo[0],
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            leadingIcon = {
                          Icon(
                              painter = painterResource(id = R.drawable.baseline_account_circle_24),
                              contentDescription = null)
            },
            onValueChange = {updateLoginInfo(it, loginInfo[1])},
            label = { stringResource(id = R.string.user_name)},
            isError = isWrong,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onNext = {focusRequester.requestFocus()}
            )
        )

        OutlinedTextField(
            value = loginInfo[1],
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            leadingIcon = {
                Icon(Icons.Default.Lock,
                    contentDescription = null)
            },
            onValueChange = { updateLoginInfo(loginInfo[0], it) },
            label = { stringResource(id = R.string.password)},
            isError = isWrong,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                {
                    painterResource(id = R.drawable.baseline_visibility_24)
                }
                else {
                    painterResource(id = R.drawable.baseline_visibility_off_24)
                }

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, description)
                }
            },
            keyboardActions = KeyboardActions(
                onDone = { checkUserNamePassword(context, loginInfo[0], loginInfo[1])}
            )
        )

        OutlinedButton(
            onClick = {checkUserNamePassword(context, loginInfo[0], loginInfo[1])}
        ) {
            Text(text = "Log in",
                color = MaterialTheme.colorScheme.secondary)
        }

        ClickableText(
            text = AnnotatedString(stringResource(id = R.string.create_new_account)),
            onClick = onClickableTextClicked,
            style = TextStyle(color = Color.Blue, fontStyle = FontStyle.Italic)
        )

    }
}

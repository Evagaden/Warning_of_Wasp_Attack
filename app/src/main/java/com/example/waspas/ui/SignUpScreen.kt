package com.example.waspas.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.WarningOfWaspAttackTheme
import com.example.waspas.AppScreen
import com.example.waspas.R
import com.example.waspas.model.User
import com.example.waspas.ui.state.UiState

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    signupState: UiState,
    navController: NavController,
    onSignUpButtonClicked: (Boolean, Context, String, String, User) -> Unit,
    ) {
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        when(signupState)
        {
            is UiState.Loading, UiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val focusRequester0 = remember {FocusRequester()}
                    val focusRequester1 = remember {FocusRequester()}
                    val focusRequester2 = remember {FocusRequester()}
                    val focusRequester3 = remember {FocusRequester()}
                    val context = LocalContext.current
                    val message = stringResource(id = R.string.sign_up_message)
                    val messageCheck = stringResource(id = R.string.checking_rules_message)
                    var check by remember{ mutableStateOf(false) }
                    var phone by remember { mutableStateOf("") }
                    var name by remember { mutableStateOf("") }
                    var email by remember { mutableStateOf("") }
                    var password by remember { mutableStateOf("") }
                    val user = User(phone = phone, name = name, email = email, password = password)
                    NormalTextComponent(value = "Hello there,")
                    HeadingTextComponent(value = "Create an Account")
                    Spacer(modifier = Modifier.height(25.dp))

                    Column {
                        MyTextFieldComponent(
                            labelValue = "Phone",
                            textValue = phone,
                            icon = Icons.Filled.Phone,
                            onValueChange = {phone = it},
                            keyboardType = KeyboardType.Number,
                            focusRequest = focusRequester0,
                            nextFocusRequester = focusRequester1
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        MyTextFieldComponent(
                            labelValue = "Name",
                            textValue = name,
                            icon = Icons.Outlined.Person,
                            onValueChange = {name = it},
                            keyboardType = KeyboardType.Text,
                            focusRequest = focusRequester1,
                            nextFocusRequester = focusRequester2
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        MyTextFieldComponent(
                            labelValue = "Email",
                            textValue = email,
                            icon = Icons.Outlined.Email,
                            onValueChange = {email = it},
                            keyboardType = KeyboardType.Email,
                            focusRequest = focusRequester2,
                            nextFocusRequester = focusRequester3
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        PasswordTextFieldComponent(
                            labelValue = "Password",
                            password = password,
                            icon = Icons.Outlined.Lock,
                            onValueChange = {password = it},
                            focusRequest = focusRequester3
                        )
                        CheckboxComponent(isChecked = check, onValueChange = {check = it})
                        ElevatedButton(
                            onClick = {onSignUpButtonClicked(check, context, message, messageCheck, user)
                            Log.d("SignUpScreen", user.phone)},
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ){
                            Text(
                                text = stringResource(id = R.string.signup),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }

            is UiState.Success -> {
                navController.navigate(AppScreen.UserScreen.name)
            }
        }
    }
}

@Composable
fun NormalTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = colorResource(id = R.color.TextColor),
        textAlign = TextAlign.Center
    )
}

@Composable
fun HeadingTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        color = colorResource(id = R.color.TextColor),
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextFieldComponent(
    labelValue: String,
    textValue: String,
    onValueChange: (String)->Unit,
    icon: ImageVector,
    keyboardType: KeyboardType,
    focusRequest: FocusRequester,
    nextFocusRequester: FocusRequester
) {
    OutlinedTextField(
        label = {
            Text(text = labelValue)
        },
        value = textValue,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            focusedLeadingIconColor = MaterialTheme.colorScheme.outline,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequest),
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "profile"
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(
            onNext = {nextFocusRequester.requestFocus()}
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextFieldComponent(
    labelValue: String,
    password: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    focusRequest: FocusRequester
) {

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    OutlinedTextField(
        label = {
            Text(text = labelValue)
        },
        value = password,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.onSecondaryContainer,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            focusedLeadingIconColor = MaterialTheme.colorScheme.outline,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequest),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "profile"
            )
        },
        trailingIcon = {
            val iconImage =
                if (isPasswordVisible) painterResource(id = R.drawable.baseline_visibility_24)
                else painterResource(id = R.drawable.baseline_visibility_off_24)
            val description = if (isPasswordVisible) "Show Password" else "Hide Password"
            IconButton(onClick = {
                isPasswordVisible = !isPasswordVisible
            }) {
                Icon(painter = iconImage, contentDescription = description)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Composable
fun CheckboxComponent(
    isChecked: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onValueChange
        )
        ClickableTextComponent()
    }
}

@Composable
fun ClickableTextComponent() {
    val initialText = "By continuing you accept our "
    val privacyPolicyText = "Privacy Policy"
    val andText = " and "
    val termOfUseText = "Term of Use"

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = colorResource(id = R.color.TextColor))) {
            append(initialText)
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
            pushStringAnnotation(tag = privacyPolicyText, annotation = privacyPolicyText)
            append(privacyPolicyText)
        }
        withStyle(style = SpanStyle(color = colorResource(id = R.color.TextColor))) {
            append(andText)
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
            pushStringAnnotation(tag = termOfUseText, annotation = termOfUseText)
            append(termOfUseText)
        }
        append(".")
    }

    ClickableText(text = annotatedString, onClick = {
        annotatedString.getStringAnnotations(it, it)
            .firstOrNull()?.also { annotation ->
                Log.d("ClickableTextComponent", "You have Clicked ${annotation.tag}")
            }
    })
}


@Composable
fun AccountQueryComponent(
    textQuery: String,
    textClickable: String,
    //navController: NavHostController
) {
    val annonatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = colorResource(id = R.color.TextColor), fontSize = 15.sp)) {
            append(textQuery)
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary, fontSize = 15.sp)) {
            pushStringAnnotation(tag = textClickable, annotation = textClickable)
            append(textClickable)
        }
    }

    ClickableText(text = annonatedString, onClick = {
        annonatedString.getStringAnnotations(it, it)
            .firstOrNull()?.also { annonation ->
                if (annonation.item == "Login") {
                    //navController.navigate("Login")
                } else if (annonation.item == "Register") {
                    //navController.navigate("Signup")
                }
            }
    })
}

@Preview(showBackground = true)
@Composable
fun TestSignUpScreen(modifier: Modifier = Modifier)
{
    WarningOfWaspAttackTheme {
        //SignUpScreen()
    }
}
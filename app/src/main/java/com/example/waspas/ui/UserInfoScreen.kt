package com.example.waspas.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.WarningOfWaspAttackTheme
import com.example.waspas.R
import com.example.waspas.model.User

@Composable
fun UserInfoScreen(
    modifier: Modifier = Modifier,
    user: User,
    onUpdateInfoButtonClicked: (Context, String, String, String, User) -> Unit,
    onLogoutButtonCLicked: ()->Unit
) {
    ElevatedCard(
        modifier = modifier
            .aspectRatio(0.5F)
            .fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
        ){
            Text(text = user.phone, Modifier.padding(dimensionResource(id = R.dimen.padding_small)))

            var name by remember {
                mutableStateOf(user.name)
            }
            TextField(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                value = name,
                onValueChange = {name = it},
                singleLine = true)

            var email by remember {
                mutableStateOf(user.email)
            }
            TextField(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                value = email,
                onValueChange = {email = it},
                singleLine = true)

            var password by remember {
                mutableStateOf(user.password)
            }
            TextField(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                value = password,
                onValueChange = {password = it},
                singleLine = true)

            Row {
                val context = LocalContext.current
                val message = stringResource(id = R.string.update_account_fail)
                val messageCheck = stringResource(id = R.string.update_account_success)
                val messageWarning = stringResource(id = R.string.update_account_warning)
                val userChanged = User(user._id, email, name, user.phone, password = password)
                OutlinedButton(
                    onClick = {onUpdateInfoButtonClicked(context, message, messageCheck, messageWarning, userChanged)}
                ) {
                    Text(text = "Update", color = MaterialTheme.colorScheme.secondary)
                }
                Spacer(Modifier.weight(1F))
                OutlinedButton(onClick = onLogoutButtonCLicked) {
                    Text(text = "Log out", color = MaterialTheme.colorScheme.secondary)
                }
            }
            Spacer(Modifier.weight(1F))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun testUserInfoScreen(modifier: Modifier = Modifier)
{
    WarningOfWaspAttackTheme {

    }
}
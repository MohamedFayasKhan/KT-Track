package com.mohamedfayaskhan.kt_track.screen.auth.forgot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mohamedfayaskhan.kt_track.R

@Composable
fun UpdatePassword(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel = viewModel<UpdatePasswordViewModel>()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.update_password),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { newValue ->
                    viewModel
                        .onEvent(
                            UpdatePasswordEvent
                                .OnEmailValueChanged(newValue)
                        )
                },
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(R.string.enter_your_mail)
                    )
                },
                label = {
                    Text(
                        text = stringResource(R.string.mail)
                    )
                },
                keyboardOptions = KeyboardOptions
                    .Default
                    .copy(
                        keyboardType = KeyboardType.Email
                    )
            )
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { newValue ->
                    viewModel
                        .onEvent(
                            UpdatePasswordEvent
                                .OnPasswordValuesChanged(newValue)
                        )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.enter_your_password)
                    )
                },
                label = {
                    Text(
                        text = stringResource(R.string.password)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions
                    .Default
                    .copy(
                        keyboardType = KeyboardType.Password
                    ),
                visualTransformation = viewModel.visualTransformationPassword,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel
                                .onEvent(
                                    UpdatePasswordEvent.OnPasswordVisibilityChanged
                                )
                        }) {
                        Icon(
                            painter = painterResource(
                                id =
                                if (viewModel.isPasswordVisible)
                                    R.drawable.visibility
                                else
                                    R.drawable.visibility_off
                            ),
                            contentDescription = null
                        )
                    }
                }
            )
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            OutlinedTextField(
                value = viewModel.repeatPassword,
                onValueChange = { newValue ->
                    viewModel
                        .onEvent(
                            UpdatePasswordEvent
                                .OnRepeatPasswordValuesChanged(newValue)
                        )
                },
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(R.string.confirm_your_password)
                    )
                },
                label = {
                    Text(
                        text = stringResource(R.string.repeat_password)
                    )
                },
                keyboardOptions = KeyboardOptions
                    .Default
                    .copy(
                        keyboardType = KeyboardType.Password
                    ),
                visualTransformation = viewModel.visualTransformationRepeatPassword,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel
                                .onEvent(
                                    UpdatePasswordEvent.OnRepeatPasswordVisibilityChanged
                                )
                        }) {
                        Icon(
                            painter = painterResource(
                                id =
                                if (viewModel.isRepeatPasswordVisible)
                                    R.drawable.visibility
                                else
                                    R.drawable.visibility_off
                            ),
                            contentDescription = null
                        )
                    }
                }
            )
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            Button(
                onClick = {
                    viewModel
                        .onEvent(
                            UpdatePasswordEvent
                                .OnForgotPassword(
                                    navController,
                                    context
                                )
                        )
                }
            ) {
                Text(
                    text = stringResource(R.string.update),
                )
            }
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
        }
    }
}
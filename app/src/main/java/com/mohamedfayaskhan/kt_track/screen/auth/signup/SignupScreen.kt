package com.mohamedfayaskhan.kt_track.screen.auth.signup

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
fun SignUpScreen(navController: NavHostController) {
    val context = LocalContext.current
    val signupViewModel = viewModel<SignupViewModel>()

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
                text = stringResource(R.string.sign_up),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            OutlinedTextField(
                value = signupViewModel.name,
                onValueChange = { newValue ->
                    signupViewModel
                        .onEvent(
                            SignUpEvent
                                .OnNameValueChanged(newValue)
                        )
                },
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(R.string.enter_your_name)
                    )
                },
                label = {
                    Text(
                        text = stringResource(R.string.name)
                    )
                },
                keyboardOptions = KeyboardOptions
                    .Default
                    .copy(
                        keyboardType = KeyboardType.Text
                    )
            )
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            OutlinedTextField(
                value = signupViewModel.email,
                onValueChange = { newValue ->
                    signupViewModel
                        .onEvent(
                            SignUpEvent
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
                        keyboardType = KeyboardType.Text
                    )
            )
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            OutlinedTextField(
                value = signupViewModel.password,
                onValueChange = { newValue ->
                    signupViewModel
                        .onEvent(
                            SignUpEvent
                                .OnPasswordValueChanged(newValue)
                        )
                },
                singleLine = true,
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
                keyboardOptions = KeyboardOptions
                    .Default
                    .copy(
                        keyboardType = KeyboardType.Password
                    ),
                visualTransformation = signupViewModel.visualTransformationPassword,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            signupViewModel
                                .onEvent(
                                    SignUpEvent.OnPasswordVisibilityChanged
                                )
                        }) {
                        Icon(
                            painter = painterResource(
                                id =
                                if (signupViewModel.isPasswordVisible)
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
                value = signupViewModel.repeatPassword,
                onValueChange = { newValue ->
                    signupViewModel
                        .onEvent(
                            SignUpEvent
                                .OnRepeatPasswordValueChanged(newValue)
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
                visualTransformation = signupViewModel.visualTransformationRepeatPassword,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            signupViewModel
                                .onEvent(
                                    SignUpEvent.OnRepeatPasswordVisibilityChanged
                                )
                        }) {
                        Icon(
                            painter = painterResource(
                                id =
                                if (signupViewModel.isRepeatPasswordVisible)
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
                    signupViewModel
                        .onEvent(
                            SignUpEvent
                                .OnCreateAccount(navController, context)
                        )
                }
            ) {
                Text(
                    text = stringResource(R.string.create_account)
                )
            }
        }
    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun SignUpPreview() {
//    SignUpScreen(navController)
//}
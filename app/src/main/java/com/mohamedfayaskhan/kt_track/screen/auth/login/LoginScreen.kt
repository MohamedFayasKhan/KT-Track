package com.mohamedfayaskhan.kt_track.screen.auth.login

import android.content.Context
import android.location.LocationManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
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
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val loginViewModel = viewModel<LoginViewModel>()

    LaunchedEffect(Unit) {
        val locationManager = context
            .getSystemService(
                Context.LOCATION_SERVICE
            ) as LocationManager
        loginViewModel
            .onEvent(
                LoginEvent
                    .OnGPSCheck(
                        locationManager.
                        isProviderEnabled(LocationManager.GPS_PROVIDER)
                    )
            )
        if (!loginViewModel.isGpsEnabled) {
            loginViewModel
                .onEvent(
                    LoginEvent
                        .EnableGPS(context)
                )
        }
    }

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
                text = stringResource(R.string.login),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            OutlinedTextField(
                value = loginViewModel.email,
                onValueChange = { newValue ->
                    loginViewModel
                        .onEvent(
                            LoginEvent
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
                value = loginViewModel.password,
                onValueChange = { newValue ->
                    loginViewModel
                        .onEvent(
                            LoginEvent
                                .OnPasswordValueChanged(newValue)
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
                visualTransformation = loginViewModel.visualTransformation,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            loginViewModel
                                .onEvent(
                                    LoginEvent.OnPasswordVisibilityChanged
                                )
                        }) {
                        Icon(
                            painter = painterResource(
                                id =
                                if (loginViewModel.isPasswordVisible)
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
                    loginViewModel
                        .onEvent(
                            LoginEvent
                                .OnLogin(
                                    navController = navController,
                                    context = context
                                )
                        )
                }
            ) {
                Text(
                    text = stringResource(id = R.string.login)
                )
            }
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Create Account",
                    modifier = Modifier
                        .clickable {
                            loginViewModel
                                .onEvent(
                                    LoginEvent
                                        .NavigateSignUp(
                                            navController
                                        )
                                )
                        }
                )
                Text(
                    text = "Forgot Password",
                    modifier = Modifier
                        .clickable {
                            loginViewModel
                                .onEvent(
                                    LoginEvent
                                        .NavigateForgotPassword(
                                            navController
                                        )
                                )
                        }
                )
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun LoginPreview() {
//    LoginScreen(navController)
//}
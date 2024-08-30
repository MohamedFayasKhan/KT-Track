package com.mohamedfayaskhan.kt_track.screen.auth.login

import android.content.Context
import androidx.navigation.NavHostController

sealed class LoginEvent {
    data class OnLogin(
        val navController: NavHostController,
        val context: Context
    ) : LoginEvent()
    data class NavigateSignUp(
        val navController: NavHostController
    ) : LoginEvent()
    data class NavigateForgotPassword(
        val navController: NavHostController
    ) : LoginEvent()
    data class OnEmailValueChanged(
        val newValue: String
    ) : LoginEvent()
    data class OnPasswordValueChanged(
        val newValue: String
    ) : LoginEvent()
    data object OnPasswordVisibilityChanged : LoginEvent()
    data class OnGPSCheck(
        val value : Boolean
    ) : LoginEvent()
    data class EnableGPS(
        val context: Context
    ) : LoginEvent()
}
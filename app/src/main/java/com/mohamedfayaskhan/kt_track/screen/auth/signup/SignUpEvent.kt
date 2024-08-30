package com.mohamedfayaskhan.kt_track.screen.auth.signup

import android.content.Context
import androidx.navigation.NavHostController

sealed class SignUpEvent {
    data class OnCreateAccount(
        val navController: NavHostController,
        val context: Context
    ) : SignUpEvent()
    data class OnNameValueChanged(
        val newValue : String
    ) : SignUpEvent()
    data class OnEmailValueChanged(
        val newValue : String
    ) : SignUpEvent()
    data class OnPasswordValueChanged(
        val newValue : String
    ) : SignUpEvent()
    data class OnRepeatPasswordValueChanged(
        val newValue : String
    ) : SignUpEvent()
    data object OnPasswordVisibilityChanged: SignUpEvent()
    data object OnRepeatPasswordVisibilityChanged: SignUpEvent()
}
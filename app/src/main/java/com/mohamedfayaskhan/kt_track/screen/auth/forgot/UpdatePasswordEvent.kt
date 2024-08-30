package com.mohamedfayaskhan.kt_track.screen.auth.forgot

import android.content.Context
import androidx.navigation.NavHostController

sealed class UpdatePasswordEvent {
    data class OnForgotPassword(
        val navController: NavHostController,
        val context: Context
    ) : UpdatePasswordEvent()
    data class OnEmailValueChanged(
        val newValue: String
    ) : UpdatePasswordEvent()
    data class OnPasswordValuesChanged(
        val newValue: String
    ) : UpdatePasswordEvent()
    data class OnRepeatPasswordValuesChanged(
        val newValue: String
    ) : UpdatePasswordEvent()
    data object OnPasswordVisibilityChanged: UpdatePasswordEvent()
    data object OnRepeatPasswordVisibilityChanged: UpdatePasswordEvent()
}
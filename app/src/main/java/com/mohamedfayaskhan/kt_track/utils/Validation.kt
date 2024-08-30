package com.mohamedfayaskhan.kt_track.utils

import android.util.Patterns


fun matchPassword(password: String, repeatPassword: String): Boolean {
    return password == repeatPassword
}

fun validateEmail(email: String, isComplete: (Boolean) -> Unit) {
    isComplete(email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
}

fun validateName(name: String, isComplete: (Boolean) -> Unit) {
    isComplete(name.isNotEmpty())
}

fun validatePassword(password: String, isComplete: (Boolean) -> Unit) {
    isComplete(password.isNotEmpty() && password.length >= 8)
}
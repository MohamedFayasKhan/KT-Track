package com.mohamedfayaskhan.kt_track.screen.auth.forgot

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamedfayaskhan.kt_track.MyApp
import com.mohamedfayaskhan.kt_track.model.User
import com.mohamedfayaskhan.kt_track.utils.matchPassword
import com.mohamedfayaskhan.kt_track.utils.validateEmail
import com.mohamedfayaskhan.kt_track.utils.validatePassword
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch

class UpdatePasswordViewModel : ViewModel() {

    private var _email by mutableStateOf("")
    val email: String get() = _email
    private var _password by mutableStateOf("")
    val password: String get() = _password
    private var _repeatPassword by mutableStateOf("")
    val repeatPassword: String get() = _repeatPassword
    private var _isPasswordVisible by mutableStateOf(false)
    val isPasswordVisible: Boolean get() = _isPasswordVisible
    private var _isRepeatPasswordVisible by mutableStateOf(false)
    val isRepeatPasswordVisible: Boolean get() = _isRepeatPasswordVisible
    private var _visualTransformationPassword by mutableStateOf(VisualTransformation.None)
    val visualTransformationPassword: VisualTransformation get() = _visualTransformationPassword
    private var _visualTransformationRepeatPassword by mutableStateOf(VisualTransformation.None)
    val visualTransformationRepeatPassword: VisualTransformation get() = _visualTransformationRepeatPassword

    init {
        _visualTransformationPassword = PasswordVisualTransformation()
        _visualTransformationRepeatPassword = PasswordVisualTransformation()
    }

    private fun forgotPassword(email: String, password: String): Boolean {
        val user = MyApp
            .realm
            .query<User>(
                query = "email == $0",
                args = arrayOf(email)
            )
            .find()

        if (user.size > 0) {
            viewModelScope
                .launch {
                    MyApp
                        .realm
                        .write {
                            findLatest(
                                user.first()
                            )?.let {
                                it.password = password
                            }
                        }
                }
        }
        return user.size > 0
    }

    fun onEvent(event: UpdatePasswordEvent) {
        when (event) {
            is UpdatePasswordEvent.OnForgotPassword -> {
                validateEmail(_email) { isEmailValid ->
                    if (isEmailValid) {
                        validatePassword(_password) { isPasswordValid ->
                            if (isPasswordValid) {
                                if (matchPassword(
                                        password = _password,
                                        repeatPassword = _repeatPassword
                                    )
                                ) {
                                    val isSuccess = forgotPassword(
                                        email = _email,
                                        password = _password
                                    )
                                    if (isSuccess) {
                                        event.navController.popBackStack()
                                    } else {
                                        Toast
                                            .makeText(
                                                event.context,
                                                "User not found",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                } else {
                                    Toast
                                        .makeText(
                                            event.context,
                                            "Password not matches",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            } else {
                                Toast
                                    .makeText(
                                        event.context,
                                        "Password must have minimum 8 character",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        }
                    } else {
                        Toast
                            .makeText(
                                event.context,
                                "Email is invalid",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                }
            }

            is UpdatePasswordEvent.OnEmailValueChanged -> _email = event.newValue

            is UpdatePasswordEvent.OnPasswordValuesChanged -> _password = event.newValue

            is UpdatePasswordEvent.OnRepeatPasswordValuesChanged -> _repeatPassword = event.newValue

            UpdatePasswordEvent.OnRepeatPasswordVisibilityChanged -> {
                _isRepeatPasswordVisible = !_isRepeatPasswordVisible
                _visualTransformationRepeatPassword = if (_isRepeatPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation()
            }

            UpdatePasswordEvent.OnPasswordVisibilityChanged -> {
                _isPasswordVisible = !_isPasswordVisible
                _visualTransformationPassword = if (_isRepeatPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation()
            }
        }
    }
}
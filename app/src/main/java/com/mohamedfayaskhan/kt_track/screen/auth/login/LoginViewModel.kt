package com.mohamedfayaskhan.kt_track.screen.auth.login

import android.content.Intent
import android.provider.Settings
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
import com.mohamedfayaskhan.kt_track.routes.Router
import com.mohamedfayaskhan.kt_track.utils.validateEmail
import com.mohamedfayaskhan.kt_track.utils.validatePassword
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private var _email by mutableStateOf("")
    val email: String get()  =_email
    private var _password by mutableStateOf("")
    val password : String get() = _password
    private var _isPasswordVisible by mutableStateOf(false)
    val isPasswordVisible : Boolean get() = _isPasswordVisible
    private var _visualTransformation by mutableStateOf(VisualTransformation.None)
    val visualTransformation : VisualTransformation get() = _visualTransformation
    private var _isGpsEnabled by mutableStateOf(true)
    val isGpsEnabled : Boolean get() = _isGpsEnabled


    init {
        _visualTransformation = PasswordVisualTransformation()
    }


    private fun login(email: String, password: String): Boolean {
        val userResult = MyApp
            .realm
            .query<User>(
                query = "email == $0 && password == $1",
                args = arrayOf(email, password)
            )
            .find()

        if (userResult.size > 0) {
            viewModelScope
                .launch {
                    MyApp
                        .realm
                        .write {
                            findLatest(
                                userResult
                                    .first()
                            )?.let {
                                it.isLogin = true
                            }
                        }
                }
            MyApp.currentUser = userResult.first()
        }
        return userResult.size > 0
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnLogin -> {
                validateEmail(_email) { isEmailValid ->
                    if (isEmailValid) {
                        validatePassword(_password) { isPasswordValid ->
                            if (isPasswordValid) {
                                val isSuccess = login(
                                        email = _email,
                                        password = _password
                                    )
                                if (isSuccess) {
                                    event.navController
                                        .navigate(
                                            Router.Main.route
                                        ) {
                                            popUpTo(Router.Login.route) {
                                                inclusive = true
                                            }
                                        }
                                    Toast
                                        .makeText(
                                            event.context,
                                            "Login Successful",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                } else {
                                    Toast
                                        .makeText(
                                            event.context,
                                            "Login failed",
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

            is LoginEvent.NavigateForgotPassword -> {
                event
                    .navController
                    .navigate(
                        Router.ForgotPassword.route
                    )
            }

            is LoginEvent.NavigateSignUp -> {
                event
                    .navController
                    .navigate(
                        Router.SignUp.route
                    )
            }

            is LoginEvent.OnEmailValueChanged -> _email = event.newValue

            is LoginEvent.OnPasswordValueChanged -> _password = event.newValue

            LoginEvent.OnPasswordVisibilityChanged -> {
                _isPasswordVisible = !_isPasswordVisible
                _visualTransformation =
                    if (_isPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation()
            }

            is LoginEvent.OnGPSCheck -> _isGpsEnabled = event.value

            is LoginEvent.EnableGPS -> {
                Toast
                    .makeText(
                        event.context,
                        "Enable location to use app efficiently",
                        Toast.LENGTH_SHORT
                    )
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                event
                    .context
                    .startActivity(intent)
            }
        }
    }
}
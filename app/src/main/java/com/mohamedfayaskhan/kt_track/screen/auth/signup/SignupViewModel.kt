package com.mohamedfayaskhan.kt_track.screen.auth.signup

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
import com.mohamedfayaskhan.kt_track.utils.validateName
import com.mohamedfayaskhan.kt_track.utils.validatePassword
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    private var _name by mutableStateOf("")
    val name : String get() = _name
    private var _email by mutableStateOf("")
    val email : String get() = _email
    private var _password by mutableStateOf("")
    val password : String get() = _password
    private var _repeatPassword by mutableStateOf("")
    val repeatPassword : String get() = _repeatPassword
    private var _isPasswordVisible by mutableStateOf(false)
    val isPasswordVisible : Boolean get() = _isPasswordVisible
    private var _isRepeatPasswordVisible by mutableStateOf(false)
    val isRepeatPasswordVisible : Boolean get() = _isRepeatPasswordVisible
    private var _visualTransformationPassword by mutableStateOf(VisualTransformation.None)
    val visualTransformationPassword : VisualTransformation get() = _visualTransformationPassword
    private var _visualTransformationRepeatPassword by mutableStateOf(VisualTransformation.None)
    val visualTransformationRepeatPassword : VisualTransformation get() = _visualTransformationRepeatPassword

    init {
        _visualTransformationPassword = PasswordVisualTransformation()
        _visualTransformationRepeatPassword = PasswordVisualTransformation()
    }

    private fun createUser(name: String, email: String, password: String): Boolean {
        val emailResult = MyApp
            .realm
            .query<User>(
                query = "email == $0",
                args = arrayOf(email)
            )
            .find()
        if (emailResult.size == 0) {
            viewModelScope
                .launch {
                    MyApp
                        .realm
                        .write {
                            val user = User().apply {
                                this.name = name
                                this.email = email
                                this.password = password
                                this.isLogin = true
                            }
                            copyToRealm(user)
                            MyApp.currentUser = user
                        }
                }
        } else {
            return false
        }
        return true
    }

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnCreateAccount -> {
                validateName(_name) { isNameValid ->
                    if (isNameValid) {
                        validateEmail(_email) { isEmailValid ->
                            if (isEmailValid) {
                                validatePassword(_password) { isPasswordValid ->
                                    if (isPasswordValid) {
                                        if (matchPassword(_password, _repeatPassword)) {
                                            val isSuccess = createUser(
                                                    name = _name,
                                                    email = _email,
                                                    password = _password
                                                )
                                            if (isSuccess) {
                                                event.navController.popBackStack()
                                                Toast
                                                    .makeText(
                                                        event.context,
                                                        "Account created successfully",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                            } else {
                                                Toast
                                                    .makeText(
                                                        event.context,
                                                        "Email already exists",
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
                                        "Invalid Email",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        }
                    } else {
                        Toast
                            .makeText(
                                event.context,
                                "Name Required",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                }
            }

            is SignUpEvent.OnEmailValueChanged -> _email = event.newValue

            is SignUpEvent.OnNameValueChanged -> _name = event.newValue

            is SignUpEvent.OnPasswordValueChanged -> _password = event.newValue

            is SignUpEvent.OnRepeatPasswordValueChanged -> _repeatPassword = event.newValue

            SignUpEvent.OnPasswordVisibilityChanged -> {
                _isPasswordVisible = !_isPasswordVisible
                _visualTransformationPassword =
                if (_isPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation()
            }

            SignUpEvent.OnRepeatPasswordVisibilityChanged -> {
                _isRepeatPasswordVisible = !_isRepeatPasswordVisible
                _visualTransformationRepeatPassword =
                    if (_isRepeatPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation()
            }
        }
    }
}
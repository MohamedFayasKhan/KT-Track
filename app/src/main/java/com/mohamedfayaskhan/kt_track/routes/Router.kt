package com.mohamedfayaskhan.kt_track.routes

sealed class Router(val route: String) {
    data object Login: Router(route = "login")
    data object SignUp: Router(route = "sign_up")
    data object ForgotPassword: Router(route = "forgot_password")
    data object Main: Router(route = "main_screen")
    data object Map: Router(route = "map_screen")
}
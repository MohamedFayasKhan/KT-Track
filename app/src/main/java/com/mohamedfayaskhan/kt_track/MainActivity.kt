package com.mohamedfayaskhan.kt_track

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mohamedfayaskhan.kt_track.routes.Router
import com.mohamedfayaskhan.kt_track.screen.auth.forgot.UpdatePassword
import com.mohamedfayaskhan.kt_track.screen.auth.login.LoginScreen
import com.mohamedfayaskhan.kt_track.screen.auth.signup.SignUpScreen
import com.mohamedfayaskhan.kt_track.screen.main.MainScreen
import com.mohamedfayaskhan.kt_track.screen.main.MapScreen
import com.mohamedfayaskhan.kt_track.ui.theme.KTTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val user = MyApp.currentUser
        val startDestination =
            if (user != null)
                Router.Main.route
            else
                Router.Login.route
        setContent {
            val navController = rememberNavController()
            KTTrackTheme {
                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable(
                        route = Router.Login.route
                    ) {
                        LoginScreen(
                            navController = navController
                        )
                    }
                    composable(
                        route = Router.SignUp.route
                    ) {
                        SignUpScreen(
                            navController = navController
                        )
                    }
                    composable(
                        route = Router.ForgotPassword.route
                    ) {
                        UpdatePassword(
                            navController = navController
                        )
                    }
                    composable(
                        route = Router.Main.route
                    ) {
                        MainScreen(
                            navController = navController,
                        )
                    }
                    composable(
                        route = Router.Map.route + "/{index}",
                        arguments = listOf(
                            navArgument("index") {
                                type = NavType.IntType
                            }
                        )
                    ) { entry ->
                        MapScreen(
                            navController = navController,
                            entry.arguments?.getInt("index")
                        )
                    }
                }
            }
        }
    }
}
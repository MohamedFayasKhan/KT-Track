package com.mohamedfayaskhan.kt_track.screen.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.mohamedfayaskhan.kt_track.MyApp
import com.mohamedfayaskhan.kt_track.model.Location
import com.mohamedfayaskhan.kt_track.model.Locations
import com.mohamedfayaskhan.kt_track.model.User
import com.mohamedfayaskhan.kt_track.routes.Router
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var _locationsData = mutableStateListOf<Location>()
    val locationsData: SnapshotStateList<Location> = _locationsData

    fun getPermission(): Array<String> {
        return arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    fun getBackgroundPermission(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        } else {
            null
        }
    }

    fun getLocations() {
        val locations = MyApp
            .realm
            .query<Locations>(
                query = "userId == $0",
                args = arrayOf(MyApp.currentUser?.id)
            ).find()

        if (locations.size > 0) {
            _locationsData
                .clear()
            _locationsData
                .addAll(
                    locations
                        .first()
                        .locations
                        .toList()
                        .sortedBy { it.time }
                )
        } else {
            _locationsData
                .clear()
        }
    }

    fun getUser(): User? {
        val user = MyApp
            .realm
            .query<User>(
                query = "isLogin == $0",
                args = arrayOf(true)
            )
            .find()
        return if (user.size > 0) user.first() else null
    }

    fun logout(navController: NavHostController, context: Context, service: Intent) {
        viewModelScope
            .launch {
                val user = getUser()
                MyApp
                    .realm
                    .write {
                        findLatest(user!!)
                            ?.let {
                                it.isLogin = false
                            }
                    }
                context.stopService(service)
                Toast
                    .makeText(
                        context,
                        "Logout successfully",
                        Toast.LENGTH_SHORT
                    )
                    .show()
                navController
                    .navigate(Router.Login.route) {
                        popUpTo(Router.Main.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
            }
    }
}
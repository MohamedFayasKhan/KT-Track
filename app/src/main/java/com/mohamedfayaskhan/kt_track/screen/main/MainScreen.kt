package com.mohamedfayaskhan.kt_track.screen.main

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mohamedfayaskhan.kt_track.R
import com.mohamedfayaskhan.kt_track.routes.Router
import com.mohamedfayaskhan.kt_track.service.LocationServices
import com.mohamedfayaskhan.kt_track.utils.getFormattedTime
import kotlinx.coroutines.delay

@Composable
fun MainScreen(navController: NavHostController) {

    val context = LocalContext.current
    val service = Intent(context, LocationServices::class.java)
    val mainViewModel = viewModel<MainViewModel>()
    val user = mainViewModel.getUser()
    val locations = mainViewModel.locationsData

    val permissions = mainViewModel.getPermission()
    val backgroundPermission = mainViewModel.getBackgroundPermission()

    val backgroundPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(
                context,
                "Background location permission not granted. Kindly enable it by \"Allow all the time\".",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            context
                .startService(service)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsResult ->
        val allPermissionsGranted = permissionsResult
            .values
            .all { it }
        if (!allPermissionsGranted) {
            Toast.makeText(
                context,
                "Foreground location permission not granted",
                Toast.LENGTH_SHORT
            ).show()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && backgroundPermission != null) {
            backgroundPermissionLauncher
                .launch(backgroundPermission)
        }
    }

    LaunchedEffect(Unit) {
        val foregroundPermissionsGranted = permissions
            .all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }

        if (!foregroundPermissionsGranted) {
            permissionLauncher
                .launch(permissions)
        }

        if (backgroundPermission != null) {
            val backgroundPermissionGranted = ContextCompat.checkSelfPermission(
                context, backgroundPermission
            ) == PackageManager.PERMISSION_GRANTED

            if (!backgroundPermissionGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                backgroundPermissionLauncher.launch(backgroundPermission)
            } else {
                context.startService(service)
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            mainViewModel.getLocations()
            delay((1 * 60 * 500) + 500)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(
                        onClick = {
                            mainViewModel
                                .logout(
                                    navController,
                                    context,
                                    service
                                )
                        }) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.power_off
                            ),
                            contentDescription = null
                        )
                    }
                }
                Text(
                    text = "Name: ${user?.name}"
                )
                Text(
                    text = "Email: ${user?.email}"
                )
            }
            itemsIndexed(locations) { index, location ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController
                                .navigate(Router.Map.route + "/${index + 1}")
                        }
                ) {
                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillParentMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = location.latitude.toString()
                        )
                        Text(
                            text = location.longitude.toString()
                        )
                    }
                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                    Text(
                        text = getFormattedTime(location.time)
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
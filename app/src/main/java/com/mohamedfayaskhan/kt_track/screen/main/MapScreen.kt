package com.mohamedfayaskhan.kt_track.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.mohamedfayaskhan.kt_track.R

@Composable
fun MapScreen(navController: NavHostController, index: Int?) {

    val mapViewModel = viewModel<MapViewModel>()
    val locations = mapViewModel.getLocationsAtIndex(index!!)
    val uiSettings by remember { mutableStateOf(MapUiSettings()) }
    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(locations.first().second, 15f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            properties = properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState
        ) {
            locations.forEach { location ->
                Marker(
                    state = rememberMarkerState(position = location.second),
                    title = "Location at ${location.first}"
                )
            }
            val polylinePoints = remember { locations.map { it.second } }
            Polyline(
                points = polylinePoints,
                color = Color.Blue,
                width = 5f
            )
        }
        IconButton(
            modifier = Modifier.padding(vertical = 16.dp),
            onClick = {
                navController.popBackStack()
            }) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = null, tint = Color.White
            )
        }
    }
}
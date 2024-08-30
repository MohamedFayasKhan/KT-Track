package com.mohamedfayaskhan.kt_track.screen.main

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.mohamedfayaskhan.kt_track.MyApp
import com.mohamedfayaskhan.kt_track.model.Locations
import com.mohamedfayaskhan.kt_track.utils.getFormattedTime
import io.realm.kotlin.ext.query

class MapViewModel: ViewModel() {

    fun getLocationsAtIndex(position: Int): List<Pair<String, LatLng>> {
        val latLngList = mutableListOf<Pair<String,LatLng>>()
        val locationsOfCurrentUser = MyApp.realm.query<Locations>(
            query = "userId == $0",
            args = arrayOf(MyApp.currentUser?.id)
        ).find().first()
        locationsOfCurrentUser.locations.take(position).forEach { location ->
            val formattedTime = getFormattedTime(location.time)
            latLngList.add(Pair(formattedTime, LatLng(location.latitude, location.longitude)))
        }
        return latLngList
    }
}
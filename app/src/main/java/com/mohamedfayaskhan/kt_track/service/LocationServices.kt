package com.mohamedfayaskhan.kt_track.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat.*
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.mohamedfayaskhan.kt_track.MainActivity
import com.mohamedfayaskhan.kt_track.MyApp
import com.mohamedfayaskhan.kt_track.R
import com.mohamedfayaskhan.kt_track.model.Locations
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.launch

class LocationServices : LifecycleService() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val channelId = "Location_Channel"
    private val notificationId = 1

    override fun onCreate() {
        super.onCreate()
        val serviceChannel = NotificationChannel(
            channelId,
            "Location_Channel",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)

        fusedLocationClient = LocationServices
            .getFusedLocationProviderClient(this)
        locationRequest = LocationRequest
            .Builder(1 * 60 * 500).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.locations.forEach { location ->
                    saveToDB(location)
                }
            }
        }

        startForeground(
            notificationId,
            getNotification()
        )

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient
                .requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
        }
    }

    private fun saveToDB(location: Location?) {
        lifecycleScope.launch {
            val locData = com.mohamedfayaskhan.kt_track.model.Location()
                .apply {
                    this.time = System.currentTimeMillis()
                    this.latitude = location?.latitude!!
                    this.longitude = location.longitude
                }
            val locationDataSearchResult = MyApp
                .realm
                .query<Locations>(
                    query = "userId == $0",
                    args = arrayOf(MyApp.currentUser?.id)
                ).find()

            MyApp
                .realm
                .write {
                    if (locationDataSearchResult.size > 0) {
                        findLatest(locationDataSearchResult.first())?.locations?.add(locData)
                    } else {
                        val loc = Locations().apply {
                            this.userId = MyApp.currentUser?.id
                            this.locations = realmListOf(locData)
                        }
                        copyToRealm(loc)
                    }
                }
        }
    }

    private fun getNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent
            .getActivity(
                this,
                3,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

        return Builder(this, channelId)
            .setContentTitle("KT-Track")
            .setContentText("Getting location")
            .setSmallIcon(R.drawable.location) // Add your own icon here
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient
            .removeLocationUpdates(locationCallback)
    }
}
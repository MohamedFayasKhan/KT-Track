package com.mohamedfayaskhan.kt_track.model

import io.realm.kotlin.types.RealmObject

class Location: RealmObject {
    var time: Long = 0L
    var latitude: Double = 0.0
    var longitude: Double = 0.0
}
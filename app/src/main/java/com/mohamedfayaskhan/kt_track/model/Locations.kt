package com.mohamedfayaskhan.kt_track.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Locations: RealmObject {
    @PrimaryKey
    var userId: ObjectId? = null
    var locations: RealmList<Location> = realmListOf()
}
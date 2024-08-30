package com.mohamedfayaskhan.kt_track

import android.app.Application
import com.mohamedfayaskhan.kt_track.model.Location
import com.mohamedfayaskhan.kt_track.model.Locations
import com.mohamedfayaskhan.kt_track.model.User
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import org.mongodb.kbson.ObjectId

class MyApp : Application() {
    companion object {
        lateinit var realm: Realm
        var currentUser: User? = null
    }

    override fun onCreate() {
        super.onCreate()
        realm = Realm
            .open(
                configuration = RealmConfiguration
                    .create(
                        schema = setOf(
                            User::class,
                            Location::class,
                            Locations::class
                        )
                    )
            )
        val user = realm.query<User>(
            query = "isLogin == $0",
            args = arrayOf(true)
        ).find()
        if (user.size > 0) {
            currentUser = user.first()
        }
    }
}
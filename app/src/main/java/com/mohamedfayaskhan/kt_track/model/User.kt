package com.mohamedfayaskhan.kt_track.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class User: RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var name: String = ""
    var email: String = ""
    var password: String = ""
    var isLogin: Boolean = false
}
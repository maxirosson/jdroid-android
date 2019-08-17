package com.jdroid.android.sample.database.room

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sample_2")
class Sample2Entity {

    @NonNull
    @PrimaryKey
    lateinit var id: String
}

package com.jdroid.android.sample.database.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date

@Entity
class SampleEntity {

    @NonNull
    @PrimaryKey
    lateinit var id: String

    @ColumnInfo(name = "field")
    var field: String? = null

    var date: Date? = null

    var stringList: List<String>? = null

    @Ignore
    var ignoredField: String? = null

    override fun toString(): String {
        val sb = StringBuffer("SampleEntity{")
        sb.append("id='").append(id).append('\'')
        sb.append(", field='").append(field).append('\'')
        sb.append(", date=").append(date)
        sb.append(", stringList=").append(stringList)
        sb.append(", ignoredField='").append(ignoredField).append('\'')
        sb.append('}')
        return sb.toString()
    }
}

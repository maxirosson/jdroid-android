package com.jdroid.android.sample.ui.firebase.database

import com.jdroid.java.domain.Entity

class SampleFirebaseEntity : Entity() {

    var field: String? = null

    override fun toString(): String {
        val builder = StringBuilder("{")
        builder.append("id='").append(getId()).append("\', ")
        builder.append("field='").append(field).append('\'')
        builder.append('}')
        return builder.toString()
    }
}

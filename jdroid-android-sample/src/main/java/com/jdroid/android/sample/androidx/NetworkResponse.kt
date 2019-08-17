package com.jdroid.android.sample.androidx

class NetworkResponse {

    lateinit var id: String
    var value: String? = null

    override fun toString(): String {
        val sb = StringBuffer("NetworkResponse{")
        sb.append("id='").append(id).append('\'')
        sb.append(", value='").append(value).append('\'')
        sb.append('}')
        return sb.toString()
    }
}

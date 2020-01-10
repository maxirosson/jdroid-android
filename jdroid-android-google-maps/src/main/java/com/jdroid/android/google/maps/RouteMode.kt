package com.jdroid.android.google.maps

import com.jdroid.android.dialog.ChoiceItem

enum class RouteMode constructor(
    val colorId: Int,
    private val resourceTitleId: Int,
    val argName: String
) : ChoiceItem {

    WALKING(R.color.jdroid_walkingColor, R.string.jdroid_walking, "walking"),
    DRIVING(R.color.jdroid_drivingColor, R.string.jdroid_driving, "driving");

    override fun getResourceTitleId(): Int {
        return resourceTitleId
    }
}

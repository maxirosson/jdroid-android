package com.jdroid.android.jetpack.work

import androidx.work.Data

fun Data.getRequiredString(key: String): String {
    return getString(key)!!
}

fun Data.getRequiredBooleanArray(key: String): BooleanArray {
    return getBooleanArray(key)!!
}

fun Data.getRequiredByteArray(key: String): ByteArray {
    return getByteArray(key)!!
}

fun Data.getRequiredIntArray(key: String): IntArray {
    return getIntArray(key)!!
}

fun Data.getRequiredLongArray(key: String): LongArray {
    return getLongArray(key)!!
}

fun Data.getRequiredFloatArray(key: String): FloatArray {
    return getFloatArray(key)!!
}

fun Data.getRequiredDoubleArray(key: String): DoubleArray {
    return getDoubleArray(key)!!
}

fun Data.getRequiredStringArray(key: String): Array<String> {
    return getStringArray(key)!!
}

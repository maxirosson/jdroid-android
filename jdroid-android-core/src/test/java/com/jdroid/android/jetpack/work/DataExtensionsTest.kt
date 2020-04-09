package com.jdroid.android.jetpack.work

import androidx.work.workDataOf
import com.google.common.truth.Truth
import org.junit.Test

class DataExtensionsTest {

    @Test
    fun `GIVEN a non null vale WHEN invoking getRequiredString THEN we get the non null vale`() {
        val value = "value"
        val data = workDataOf("key" to value)
        Truth.assertThat(data.getRequiredString("key")).isEqualTo(value)
    }

    @Test(expected = KotlinNullPointerException::class)
    fun `GIVEN a null vale WHEN invoking getRequiredString THEN we get an exception`() {
        val data = workDataOf("key" to null)
        Truth.assertThat(data.getRequiredString("key"))
    }

    @Test
    fun `GIVEN a non null vale WHEN invoking getRequiredByteArray THEN we get the non null vale`() {
        val value = "1".toByteArray()
        val data = workDataOf("key" to value)
        Truth.assertThat(data.getRequiredByteArray("key")).isEqualTo(value)
    }

    @Test(expected = KotlinNullPointerException::class)
    fun `GIVEN a null vale WHEN invoking getRequiredByteArray THEN we get an exception`() {
        val data = workDataOf("key" to null)
        Truth.assertThat(data.getRequiredByteArray("key"))
    }

    @Test
    fun `GIVEN a non null vale WHEN invoking getRequiredIntArray THEN we get the non null vale`() {
        val value = intArrayOf(1, 2, 3)
        val data = workDataOf("key" to value)
        Truth.assertThat(data.getRequiredIntArray("key")).isEqualTo(value)
    }

    @Test(expected = KotlinNullPointerException::class)
    fun `GIVEN a null vale WHEN invoking getRequiredIntArray THEN we get an exception`() {
        val data = workDataOf("key" to null)
        Truth.assertThat(data.getRequiredIntArray("key"))
    }

    @Test
    fun `GIVEN a non null vale WHEN invoking getRequiredBooleanArray THEN we get the non null vale`() {
        val value = booleanArrayOf(true, false)
        val data = workDataOf("key" to value)
        Truth.assertThat(data.getRequiredBooleanArray("key")).isEqualTo(value)
    }

    @Test(expected = KotlinNullPointerException::class)
    fun `GIVEN a null vale WHEN invoking getRequiredBooleanArray THEN we get an exception`() {
        val data = workDataOf("key" to null)
        Truth.assertThat(data.getRequiredBooleanArray("key"))
    }

    @Test
    fun `GIVEN a non null vale WHEN invoking getRequiredLongArray THEN we get the non null vale`() {
        val value = longArrayOf(1, 2)
        val data = workDataOf("key" to value)
        Truth.assertThat(data.getRequiredLongArray("key")).isEqualTo(value)
    }

    @Test(expected = KotlinNullPointerException::class)
    fun `GIVEN a null vale WHEN invoking getRequiredLongArray THEN we get an exception`() {
        val data = workDataOf("key" to null)
        Truth.assertThat(data.getRequiredLongArray("key"))
    }

    @Test
    fun `GIVEN a non null vale WHEN invoking getRequiredDoubleArray THEN we get the non null vale`() {
        val value = doubleArrayOf(1.1, 2.2)
        val data = workDataOf("key" to value)
        Truth.assertThat(data.getRequiredDoubleArray("key")).isEqualTo(value)
    }

    @Test(expected = KotlinNullPointerException::class)
    fun `GIVEN a null vale WHEN invoking getRequiredDoubleArray THEN we get an exception`() {
        val data = workDataOf("key" to null)
        Truth.assertThat(data.getRequiredDoubleArray("key"))
    }

    @Test
    fun `GIVEN a non null vale WHEN invoking getRequiredFloatArray THEN we get the non null vale`() {
        val value = floatArrayOf(1.1F, 2.2F)
        val data = workDataOf("key" to value)
        Truth.assertThat(data.getRequiredFloatArray("key")).isEqualTo(value)
    }

    @Test(expected = KotlinNullPointerException::class)
    fun `GIVEN a null vale WHEN invoking getRequiredFloatArray THEN we get an exception`() {
        val data = workDataOf("key" to null)
        Truth.assertThat(data.getRequiredFloatArray("key"))
    }

    @Test
    fun `GIVEN a non null vale WHEN invoking getRequiredStringArray THEN we get the non null vale`() {
        val value = arrayOf("1", "2")
        val data = workDataOf("key" to value)
        Truth.assertThat(data.getRequiredStringArray("key")).isEqualTo(value)
    }

    @Test(expected = KotlinNullPointerException::class)
    fun `GIVEN a null vale WHEN invoking getRequiredStringArray THEN we get an exception`() {
        val data = workDataOf("key" to null)
        Truth.assertThat(data.getRequiredStringArray("key"))
    }
}

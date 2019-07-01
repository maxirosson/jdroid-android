package com.jdroid.android.sample.androidx

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdroid.android.androidx.lifecycle.ApiResponse
import com.jdroid.android.androidx.lifecycle.ApiSuccessResponse
import com.jdroid.android.androidx.lifecycle.DatabaseBoundResource
import com.jdroid.android.androidx.lifecycle.NetworkBoundResource
import com.jdroid.android.androidx.lifecycle.Resource
import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.room.RoomHelper
import com.jdroid.android.sample.database.room.AppDatabase
import com.jdroid.android.sample.database.room.SampleEntity
import com.jdroid.android.sample.database.room.SampleEntityDao
import com.jdroid.java.collections.Lists
import com.jdroid.java.concurrent.ExecutorUtils
import com.jdroid.java.date.DateUtils
import com.jdroid.java.exception.UnexpectedException
import com.jdroid.java.utils.RandomUtils
import java.util.concurrent.TimeUnit

object SampleRepository {

    const val ID = "1"

    fun removeItem(id: String) {
        AppExecutors.diskIOExecutor.execute { getSampleEntityDao().delete(id) }
    }

    fun addItem() {
        AppExecutors.diskIOExecutor.execute {
            val sampleEntity = SampleEntity()
            sampleEntity.id = RandomUtils.getLong().toString()
            sampleEntity.field = RandomUtils.getLong().toString()
            sampleEntity.date = DateUtils.now()
            getSampleEntityDao().upsert(sampleEntity)
        }
    }

    fun getItem(
        id: String,
        forceRefresh: Boolean,
        failLoadFromNetwork: Boolean,
        failLoadFromDb: Boolean,
        failSaveToDb: Boolean,
        loadFromNetworkDelaySeconds: Int,
        loadFromDbDelaySeconds: Int,
        saveToDbDelaySeconds: Int
    ): LiveData<Resource<SampleEntity>> {
        return object : NetworkBoundResource<SampleEntity, SampleEntity, NetworkResponse>() {

            @WorkerThread
            override fun saveToDb(item: NetworkResponse) {
                ExecutorUtils.sleep(saveToDbDelaySeconds, TimeUnit.SECONDS)
                if (failSaveToDb) {
                    throw UnexpectedException("Sample save to db failed")
                } else {
                    val sampleEntity = SampleEntity()
                    sampleEntity.id = item.id
                    sampleEntity.field = item.value
                    sampleEntity.date = DateUtils.now()
                    getSampleEntityDao().upsert(sampleEntity)
                }
            }

            override fun shouldFetch(data: SampleEntity?): Boolean {
                return forceRefresh || data == null || data.date != null && DateUtils.nowMillis() - data.date.time > 10000
            }

            @MainThread
            override fun loadFromDb(): LiveData<SampleEntity> {
                if (loadFromDbDelaySeconds > 0) {
                    val liveData = MutableLiveData<SampleEntity>()
                    AppExecutors.diskIOExecutor.execute {
                        ExecutorUtils.sleep(loadFromDbDelaySeconds, TimeUnit.SECONDS)
                        if (failLoadFromDb) {
                            // TODO This is not properly simulating on error when loading the database
                            throw UnexpectedException("Sample load from db failed")
                        } else {
                            val sampleEntity = getSampleEntityDao().getSync(id)
                            liveData.postValue(sampleEntity)
                        }
                    }
                    return liveData
                } else {
                    if (failLoadFromDb) {
                        AppExecutors.diskIOExecutor.execute { throw UnexpectedException("Sample load from db failed") }
                        return MutableLiveData()
                    } else {
                        return getSampleEntityDao().get(id)
                    }
                }
            }

            @WorkerThread
            override fun doLoadFromNetwork(): ApiResponse<NetworkResponse> {
                // Simulate a request
                ExecutorUtils.sleep(loadFromNetworkDelaySeconds, TimeUnit.SECONDS)
                if (failLoadFromNetwork) {
                    throw UnexpectedException("Sample network request failed")
                } else {
                    val networkResponse = NetworkResponse()
                    networkResponse.id = ID
                    networkResponse.value = RandomUtils.getLong().toString()
                    return ApiSuccessResponse(networkResponse)
                }
            }
        }.liveData
    }

    fun getAll(forceRefresh: Boolean, failExecution: Boolean): LiveData<Resource<List<SampleEntity>>> {
        return object : NetworkBoundResource<List<SampleEntity>, List<SampleEntity>, List<NetworkResponse>>() {

            override fun saveToDb(items: List<NetworkResponse>) {
                val entities = Lists.newArrayList<SampleEntity>()
                for (networkResponse in items) {
                    val sampleEntity = SampleEntity()
                    sampleEntity.id = networkResponse.id
                    sampleEntity.field = networkResponse.value
                    sampleEntity.date = DateUtils.now()
                    entities.add(sampleEntity)
                }
                getSampleEntityDao().replaceAll(entities)
            }

            override fun shouldFetch(data: List<SampleEntity>?): Boolean {
                return forceRefresh || data == null
            }

            override fun loadFromDb(): LiveData<List<SampleEntity>> {
                return getSampleEntityDao().allLiveData
            }

            override fun doLoadFromNetwork(): ApiResponse<List<NetworkResponse>> {
                // Simulate a request
                ExecutorUtils.sleep(5, TimeUnit.SECONDS)
                if (failExecution) {
                    throw UnexpectedException("Sample network request failed")
                } else {
                    val networkResponses = Lists.newArrayList<NetworkResponse>()
                    val networkResponse = NetworkResponse()
                    networkResponse.id = ID
                    networkResponse.value = RandomUtils.getLong().toString()
                    networkResponses.add(networkResponse)

                    return ApiSuccessResponse(networkResponses)
                }
            }
        }.liveData
    }

    fun getStrings(): LiveData<Resource<List<String>>> {
        return object : DatabaseBoundResource<List<String>>() {

            override fun loadFromDb(): LiveData<List<String>> {
                val items = Lists.newArrayList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen")
                val liveData = MutableLiveData<List<String>>()
                liveData.value = items
                return liveData
            }
        }.liveData
    }

    fun getMixedTypes(): LiveData<Resource<List<Any>>> {
        return object : DatabaseBoundResource<List<Any>>() {

            override fun loadFromDb(): LiveData<List<Any>> {
                val items = Lists.newArrayList<Any>("one", "two", true, "three", 1, 2, "four", true, "five", "six", "seven", "eight", 3, "nine", "ten", "eleven", "twelve", 4, "thirteen", false, false, "fourteen", "fifteen", "sixteen")
                val liveData = MutableLiveData<List<Any>>()
                liveData.value = items
                return liveData
            }
        }.liveData
    }

    private fun getSampleEntityDao(): SampleEntityDao {
        return RoomHelper.getDefaultDatabase(AppDatabase::class.java).sampleEntityDao()
    }
}

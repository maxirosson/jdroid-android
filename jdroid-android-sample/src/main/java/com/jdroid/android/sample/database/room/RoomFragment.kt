package com.jdroid.android.sample.database.room

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.room.RoomHelper
import com.jdroid.android.sample.R
import com.jdroid.java.date.DateUtils
import com.jdroid.java.utils.RandomUtils

class RoomFragment : AbstractFragment() {

    companion object {
        private var lastId: String? = null
    }

    override fun getContentFragmentLayout(): Int? {
        return R.layout.room_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.add).setOnClickListener {
            AppExecutors.diskIOExecutor.execute {
                val sampleEntityDao = RoomHelper.getDefaultDatabase(AppDatabase::class.java).sampleEntityDao()
                val entity = SampleEntity()
                lastId = RandomUtils.getLong().toString()
                entity.id = lastId!!
                entity.field = RandomUtils.getLong().toString()
                entity.date = DateUtils.now()
                entity.stringList = listOf("a", "b", "c")
                sampleEntityDao.insert(entity)
            }
        }
        findView<View>(R.id.update).setOnClickListener {
            AppExecutors.diskIOExecutor.execute {
                val sampleEntityDao = RoomHelper.getDefaultDatabase(AppDatabase::class.java).sampleEntityDao()
                val entity = SampleEntity()
                entity.id = lastId!!
                entity.field = RandomUtils.getLong().toString()
                entity.date = DateUtils.now()
                entity.stringList = listOf("a", "b", "c")
                sampleEntityDao.update(entity)
            }
        }
        findView<View>(R.id.upsert).setOnClickListener {
            AppExecutors.diskIOExecutor.execute {
                val sampleEntityDao = RoomHelper.getDefaultDatabase(AppDatabase::class.java).sampleEntityDao()
                val entity = SampleEntity()
                lastId = RandomUtils.getLong().toString()
                entity.id = lastId!!
                entity.field = RandomUtils.getLong().toString()
                entity.date = DateUtils.now()
                entity.stringList = listOf("a", "b", "c")
                sampleEntityDao.upsert(entity)
            }
        }
        findView<View>(R.id.remove).setOnClickListener {
            AppExecutors.diskIOExecutor.execute {
                if (lastId != null) {
                    val sampleEntityDao = RoomHelper.getDefaultDatabase(AppDatabase::class.java).sampleEntityDao()
                    sampleEntityDao.delete(lastId!!)
                    lastId = null
                }
            }
        }
        findView<View>(R.id.removeAll).setOnClickListener {
            AppExecutors.diskIOExecutor.execute {
                val sampleEntityDao = RoomHelper.getDefaultDatabase(AppDatabase::class.java).sampleEntityDao()
                sampleEntityDao.deleteAll()
            }
        }
        findView<View>(R.id.insertAll).setOnClickListener {
            AppExecutors.diskIOExecutor.execute {
                val sampleEntityDao = RoomHelper.getDefaultDatabase(AppDatabase::class.java).sampleEntityDao()

                val entities = mutableListOf<SampleEntity>()

                var entity = SampleEntity()
                lastId = RandomUtils.getLong().toString()
                entity.id = lastId!!
                entity.field = RandomUtils.getLong().toString()
                entities.add(entity)

                entity = SampleEntity()
                lastId = RandomUtils.getLong().toString()
                entity.id = lastId!!
                entity.field = RandomUtils.getLong().toString()
                entities.add(entity)

                sampleEntityDao.insertAll(entities)
            }
        }

        findView<View>(R.id.getAll).setOnClickListener {
            AppExecutors.diskIOExecutor.execute {
                val sampleEntityDao = RoomHelper.getDefaultDatabase(AppDatabase::class.java).sampleEntityDao()
                val results = sampleEntityDao.getAll()
                executeOnUIThread(Runnable { (findView<View>(R.id.results) as TextView).text = results.toString() })
            }
        }
    }
}

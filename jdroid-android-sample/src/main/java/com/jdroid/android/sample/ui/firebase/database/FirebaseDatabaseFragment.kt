package com.jdroid.android.sample.ui.firebase.database

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.java.utils.RandomUtils

class FirebaseDatabaseFragment : AbstractFragment() {

    companion object {
        private var lastId: String? = null
    }

    private lateinit var repository: SampleFirebaseRepository

    override fun getContentFragmentLayout(): Int? {
        return R.layout.firebase_database_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = SampleFirebaseRepository()

        findView<View>(R.id.firebaseCreate).setOnClickListener {
            AppExecutors.networkIOExecutor.execute {
                val entity = SampleFirebaseEntity()
                lastId = RandomUtils.getLong().toString()
                entity.id = lastId
                entity.field = RandomUtils.getLong().toString()
                repository.add(entity)
            }
        }

        findView<View>(R.id.firebaseUpdate).setOnClickListener {
            AppExecutors.networkIOExecutor.execute {
                val entity = SampleFirebaseEntity()
                entity.id = lastId
                entity.field = RandomUtils.getLong().toString()
                repository.update(entity)
            }
        }

        findView<View>(R.id.firebaseGetAll).setOnClickListener {
            AppExecutors.networkIOExecutor.execute {
                val results = repository.all
                executeOnUIThread(Runnable { (findView<View>(R.id.results) as TextView).text = results.toString() })
            }
        }

        findView<View>(R.id.firebaseRemove).setOnClickListener {
            AppExecutors.networkIOExecutor.execute {
                repository.remove(lastId)
                lastId = null
            }
        }
    }
}

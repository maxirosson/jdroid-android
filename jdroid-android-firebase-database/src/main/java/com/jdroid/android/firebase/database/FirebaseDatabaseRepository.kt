package com.jdroid.android.firebase.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jdroid.android.firebase.database.auth.FirebaseAuthenticationStrategy
import com.jdroid.java.domain.Entity
import com.jdroid.java.exception.UnexpectedException
import com.jdroid.java.repository.Repository
import com.jdroid.java.utils.LoggerUtils

abstract class FirebaseDatabaseRepository<T : Entity> : Repository<T> {

    companion object {
        private val LOGGER = LoggerUtils.getLogger(FirebaseDatabaseRepository::class.java)
    }

    private val firebaseAuthenticationStrategy: FirebaseAuthenticationStrategy? by lazy { createFirebaseAuthenticationStrategy() }

    protected open fun createFirebaseAuthenticationStrategy(): FirebaseAuthenticationStrategy? {
        return null
    }

    protected abstract fun getPath(): String

    protected abstract fun getEntityClass(): Class<T>

    protected open fun getDatabaseUrl(): String? {
        return null
    }

    protected open fun createDatabaseReference(): DatabaseReference {
        val firebaseDatabase: FirebaseDatabase?
        firebaseDatabase = if (getDatabaseUrl() != null) {
            FirebaseDatabase.getInstance(getDatabaseUrl()!!)
        } else {
            FirebaseDatabase.getInstance()
        }
        var databaseReference = firebaseDatabase.reference
        firebaseAuthenticationStrategy?.authenticate(databaseReference)
        databaseReference = databaseReference.child(getPath())
        return databaseReference
    }

    override operator fun get(id: String): T? {
        var databaseReference = createDatabaseReference()
        databaseReference = databaseReference.child(id)
        val listener = FirebaseDatabaseValueEventListener()
        databaseReference.addListenerForSingleValueEvent(listener)
        listener.waitOperation()
        val result = listener.dataSnapshot.getValue(getEntityClass())
        LOGGER.info("Retrieved object from database of path: ${getPath()}. [ $result ]")
        return result
    }

    override fun add(item: T) {
        var databaseReference = createDatabaseReference()
        databaseReference = if (item.getId() != null) {
            databaseReference.child(item.getId()!!)
        } else {
            databaseReference.push()
        }

        val listener = FirebaseDatabaseCompletionListener()
        databaseReference.setValue(item, listener)

        listener.waitOperation()
        if (item.getId() == null) {
            // Add the id field
            addIdField(databaseReference.key)
        }
        item.setId(databaseReference.key!!)
        LOGGER.info("Stored object in database: $item")
    }

    private fun addIdField(id: String?) {
        var databaseReference = createDatabaseReference()
        databaseReference = databaseReference.child(id!!)

        val map = mutableMapOf<String, Any>()
        map["id"] = id

        val listener = FirebaseDatabaseCompletionListener()
        databaseReference.updateChildren(map, listener)
        listener.waitOperation()
    }

    override fun addAll(items: Collection<T>) {
        for (each in items) {
            add(each)
        }
    }

    override fun update(item: T) {
        if (item.getId() == null) {
            throw UnexpectedException("Item with null id can not be updated")
        }
        add(item)
    }

    override fun getByField(fieldName: String, vararg values: Any): List<T> {
        val databaseReference = createDatabaseReference()
        var query = databaseReference.orderByChild(fieldName)

        if (values.size > 1) {
            throw UnexpectedException("Just one value is supported")
        }
        val value = values[0]
        query = when (value) {
            is String -> query.equalTo(value)
            is Long -> query.equalTo(value.toDouble())
            is Double -> query.equalTo(value)
            is Int -> query.equalTo(value.toDouble())
            is Boolean -> query.equalTo(value)
            else -> throw UnexpectedException("Value type not supported")
        }

        val listener = FirebaseDatabaseValueEventListener()
        query.addListenerForSingleValueEvent(listener)
        listener.waitOperation()
        val results = mutableListOf<T>()
        for (eachSnapshot in listener.dataSnapshot.children) {
            eachSnapshot.getValue(getEntityClass())?.let { results.add(it) }
        }
        LOGGER.info("Retrieved objects [" + results.size + "] from database of path: " + getPath() + " field: " + fieldName)
        return results
    }

    override fun getItemByField(fieldName: String, vararg values: Any): T? {
        val items = getByField(fieldName, *values)
        return if (items.isNotEmpty()) {
            items[0]
        } else {
            null
        }
    }

    override fun getAll(): List<T> {
        val databaseReference = createDatabaseReference()
        val listener = FirebaseDatabaseValueEventListener()
        databaseReference.addListenerForSingleValueEvent(listener)
        listener.waitOperation()
        val results = mutableListOf<T>()
        for (eachSnapshot in listener.dataSnapshot.children) {
            eachSnapshot.getValue(getEntityClass())?.let { results.add(it) }
        }
        LOGGER.info("Retrieved all objects [" + results.size + "] from path: " + getPath())
        return results
    }

    override fun getByIds(ids: List<String>): List<T> {
        val databaseReference = createDatabaseReference()
        val listener = FirebaseDatabaseValueEventListener()
        databaseReference.addListenerForSingleValueEvent(listener)
        listener.waitOperation()
        val results = mutableListOf<T>()
        for (eachSnapshot in listener.dataSnapshot.children) {
            val each = eachSnapshot.getValue(getEntityClass())
            if (ids.contains(each!!.getId())) {
                results.add(each)
            }
        }
        LOGGER.info("Retrieved all objects [" + results.size + "] from path: " + getPath() + " and ids: " + ids)
        return results
    }

    override fun remove(item: T) {
        remove(item.getId()!!)
    }

    override fun removeAll() {
        innerRemove(null)
    }

    override fun removeAll(items: Collection<T>) {
        for (each in items) {
            remove(each)
        }
    }

    override fun remove(id: String) {
        innerRemove(id)
    }

    private fun innerRemove(id: String?) {
        var databaseReference = createDatabaseReference()
        if (id != null) {
            databaseReference = databaseReference.child(id)
        }

        val listener = FirebaseDatabaseCompletionListener()
        databaseReference.removeValue(listener)
        listener.waitOperation()
        LOGGER.trace("Deleted object in database: with id: " + id!!)
    }

    override fun isEmpty(): Boolean {
        return getSize().equals(0)
    }

    override fun getSize(): Long {
        val databaseReference = createDatabaseReference()
        val listener = FirebaseDatabaseValueEventListener()
        databaseReference.addListenerForSingleValueEvent(listener)
        listener.waitOperation()
        return listener.dataSnapshot.childrenCount
    }

    override fun replaceAll(items: Collection<T>) {
        for (each in items) {
            update(each)
        }
    }

    override fun getUniqueInstance(): T? {
        val results = getAll()
        return if (results.isNotEmpty()) {
            results[0]
        } else null
    }
}

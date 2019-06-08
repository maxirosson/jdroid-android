package com.jdroid.android.domain

import com.jdroid.java.domain.Entity

class SyncEntity : Entity() {

    var syncStatus: SyncStatus? = null

    fun markAsSynced() {
        syncStatus = SyncStatus.SYNCED
    }
}

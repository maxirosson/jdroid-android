package com.jdroid.android.application

import androidx.annotation.WorkerThread
import com.jdroid.java.utils.LoggerUtils

internal class UpdateManager {

    companion object {
        private val LOGGER = LoggerUtils.getLogger(UpdateManager::class.java)
    }

    private val updatedSteps = mutableListOf<UpdateStep>()

    @WorkerThread
    fun update(fromVersionCode: Int) {
        for (step in updatedSteps) {
            if (step.getVersionCode() > fromVersionCode) {
                LOGGER.info("Started update: " + step.javaClass.simpleName + " - From version code: " + fromVersionCode)
                step.update()
                LOGGER.info("Finished update: " + step.javaClass.simpleName)
            }
        }
    }

    fun addUpdateStep(updateStep: UpdateStep) {
        updatedSteps.add(updateStep)
    }

    fun addUpdateSteps(updateSteps: List<UpdateStep>?) {
        if (updateSteps != null) {
            updatedSteps.addAll(updateSteps)
        }
    }
}

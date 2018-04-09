package com.jdroid.android.application;

import android.support.annotation.WorkerThread;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;

class UpdateManager {

	private static final Logger LOGGER = LoggerUtils.getLogger(UpdateManager.class);

	private List<UpdateStep> updatedSteps = Lists.newArrayList();

	@WorkerThread
	public void update(Integer fromVersionCode) {
		for (UpdateStep step : updatedSteps) {
			if (step.getVersionCode() > fromVersionCode) {
				LOGGER.info("Started update: " + step.getClass().getSimpleName() + " - From version code: " + fromVersionCode);
				step.update();
				LOGGER.info("Finished update: " + step.getClass().getSimpleName());
			}
		}
	}

	public void addUpdateStep(UpdateStep updateStep) {
		updatedSteps.add(updateStep);
	}

	public void addUpdateSteps(List<UpdateStep> updateSteps) {
		if (updateSteps != null) {
			updatedSteps.addAll(updateSteps);
		}
	}
}

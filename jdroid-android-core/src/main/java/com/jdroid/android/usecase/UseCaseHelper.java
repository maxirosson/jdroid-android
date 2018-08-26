package com.jdroid.android.usecase;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;

import com.jdroid.android.usecase.listener.UseCaseListener;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.concurrent.TimeUnit;

public class UseCaseHelper {

	@MainThread
	public static void registerUseCase(AbstractUseCase useCase, UseCaseListener listener) {
		registerUseCase(useCase, listener, UseCaseTrigger.MANUAL);
	}

	@MainThread
	public static void registerUseCase(final AbstractUseCase useCase, final UseCaseListener listener,
									   final UseCaseTrigger useCaseTrigger) {
		if (useCase != null) {
			useCase.addListener(listener);
			useCase.setHandler(new Handler(Looper.getMainLooper()));
			if (useCase.isInProgress()) {
				if (listener != null && !useCase.isNotified()) {
					useCase.notifyUseCaseStart(listener);
				}
			} else if (useCase.isFinishSuccessful()) {
				if (listener != null && !useCase.isNotified()) {
					useCase.notifyFinishedUseCase(listener);
					useCase.markAsNotified();
				}

				if (useCaseTrigger.equals(UseCaseTrigger.ALWAYS)) {
					executeUseCase(useCase);
				}
			} else if (useCase.isFinishFailed()) {
				if (listener != null && !useCase.isNotified()) {
					useCase.notifyFailedUseCase(useCase.getAbstractException(), listener);
					useCase.markAsNotified();
				}

				if (useCaseTrigger.equals(UseCaseTrigger.ALWAYS)) {
					executeUseCase(useCase);
				}

			} else if (useCase.isNotInvoked()
				&& (useCaseTrigger.equals(UseCaseTrigger.ONCE) || useCaseTrigger.equals(UseCaseTrigger.ALWAYS))) {
				executeUseCase(useCase);
			}
		}
	}

	@MainThread
	public static void unregisterUseCase(AbstractUseCase userCase, UseCaseListener listener) {
		if (userCase != null) {
			userCase.removeListener(listener);
		}
	}

	public static void executeUseCase(AbstractUseCase useCase) {
		ExecutorUtils.execute(useCase);
	}

	public static void executeUseCase(AbstractUseCase useCase, Long delay, TimeUnit timeUnit) {
		ExecutorUtils.schedule(useCase, delay, timeUnit);
	}
}

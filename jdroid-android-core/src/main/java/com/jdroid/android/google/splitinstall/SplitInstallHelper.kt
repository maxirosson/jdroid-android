package com.jdroid.android.google.splitinstall

import com.google.android.play.core.splitinstall.SplitInstallException
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.koin.KoinHelper
import com.jdroid.java.utils.LoggerUtils
import org.koin.core.get
import org.slf4j.Logger

object SplitInstallHelper {

    val LOGGER: Logger = LoggerUtils.getLogger("Split")

    fun getInstalledModules(): Set<String> {
        val splitInstallManager = SplitInstallManagerFactory.create(AbstractApplication.get())
        return splitInstallManager.installedModules
    }

    fun installModule(moduleName: String, moduleInstallListener: ModuleInstallListener) {
        val splitInstallManager = SplitInstallManagerFactory.create(AbstractApplication.get())
        if (!splitInstallManager.installedModules.contains(moduleName)) {

            // Creates a request to install a module.
            val request = SplitInstallRequest.newBuilder().addModule(moduleName).build()

            // Initializes a variable to later track the session ID for a given request.
            var mySessionId = 0

            // Creates a listener for request status updates.
            val listener = object : SplitInstallStateUpdatedListener {

                override fun onStateUpdate(state: SplitInstallSessionState) {
                    if (state.status() == SplitInstallSessionStatus.FAILED &&
                        state.errorCode() == SplitInstallErrorCode.SERVICE_DIED) {
                        // Retry the request.
                        moduleInstallListener.onFailure()
                        splitInstallManager.unregisterListener(this)
                    } else if (state.sessionId() == mySessionId) {

                        splitInstallManager.startConfirmationDialogForResult(state, null, 1)
                        when (state.status()) {

                            // The request has been accepted and the download should start soon.
                            SplitInstallSessionStatus.PENDING -> {
                                LOGGER.debug("Split install [$moduleName] pending")
                            }
                            // The download requires user confirmation. This is most likely due to the size of the download being larger than 10 MB.
                            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                                LOGGER.debug("Split install [$moduleName] requires user confirmation")
                                AbstractApplication.get().coreAnalyticsSender.trackSplitInstallStatus(moduleName, state)
                                moduleInstallListener.onRequiresUserConfirmation(ConfirmationDialogLauncher(splitInstallManager, state))
                            }
                            // Download is in progress.
                            SplitInstallSessionStatus.DOWNLOADING -> {
                                val totalBytes = state.totalBytesToDownload()
                                val progress = state.bytesDownloaded()
                                LOGGER.debug("Split install [$moduleName] downloading $progress/$totalBytes bytes")
                                moduleInstallListener.onDownloadInProgress(progress, totalBytes)
                            }
                            // The device has downloaded the module but installation has no yet begun.
                            SplitInstallSessionStatus.DOWNLOADED -> {
                                LOGGER.debug("Split install [$moduleName] downloaded")
                                AbstractApplication.get().coreAnalyticsSender.trackSplitInstallStatus(moduleName, state)
                            }
                            // The device is currently installing the module.
                            SplitInstallSessionStatus.INSTALLING -> {
                                LOGGER.debug("Split install [$moduleName] installing")
                            }
                            // The module is installed on the device.
                            SplitInstallSessionStatus.INSTALLED -> {
                                LOGGER.debug("Split install [$moduleName] installed")
                                AbstractApplication.get().coreAnalyticsSender.trackSplitInstallStatus(moduleName, state)
                                moduleInstallListener.onSuccess()
                                splitInstallManager.unregisterListener(this)
                            }
                            // The request failed before the module was installed on the device.
                            SplitInstallSessionStatus.FAILED -> {
                                LOGGER.debug("Split install [$moduleName] failed: " + state.errorCode())
                                AbstractApplication.get().coreAnalyticsSender.trackSplitInstallStatus(moduleName, state)
                                moduleInstallListener.onFailure()
                                splitInstallManager.unregisterListener(this)
                            }
                            // The device is in the process of cancelling the request.
                            SplitInstallSessionStatus.CANCELING -> {
                                LOGGER.debug("Split install [$moduleName] cancelling")
                            }
                            // The request has been cancelled.
                            SplitInstallSessionStatus.CANCELED -> {
                                LOGGER.debug("Split install [$moduleName] canceled")
                                AbstractApplication.get().coreAnalyticsSender.trackSplitInstallStatus(moduleName, state)
                                moduleInstallListener.onFailure()
                                splitInstallManager.unregisterListener(this)
                            }
                        }
                    }
                }
            }

            // Registers the listener.
            splitInstallManager.registerListener(listener)

            splitInstallManager.startInstall(request)
                // When the platform accepts your request to download
                // an on demand module, it binds it to the following session ID.
                // You use this ID to track further status updates for the request.
                .addOnSuccessListener { sessionId -> mySessionId = sessionId }
                // Handle any errors processing the request
                .addOnFailureListener { exception ->
                    when ((exception as SplitInstallException).errorCode) {
                        // The request is rejected because there is at least one existing request that is currently downloading.
                        SplitInstallErrorCode.ACTIVE_SESSIONS_LIMIT_EXCEEDED -> {
                            handleError(moduleName, exception, "ACTIVE_SESSIONS_LIMIT_EXCEEDED")
                        }
                        // Google Play is unable to find the requested module based on the current installed version of the app, device, and user’s Google Play account.
                        SplitInstallErrorCode.MODULE_UNAVAILABLE -> {
                            handleError(moduleName, exception, "MODULE_UNAVAILABLE")
                        }
                        // Google Play received the request, but the request is not valid.
                        SplitInstallErrorCode.INVALID_REQUEST -> {
                            handleError(moduleName, exception, "INVALID_REQUEST")
                        }
                        // A session for a given session ID was not found.
                        SplitInstallErrorCode.SESSION_NOT_FOUND -> {
                            handleError(moduleName, exception, "SESSION_NOT_FOUND")
                        }
                        // The Play Core Library is not supported on the current device. That is, the device is not able to download and install features on demand.
                        SplitInstallErrorCode.API_NOT_AVAILABLE -> {
                            handleError(moduleName, exception, "API_NOT_AVAILABLE")
                        }
                        // The app is unable to register the request because of insufficient permissions.
                        // This typically occurs when the app is in the background. Attempt the request when the app returns to the foreground.
                        SplitInstallErrorCode.ACCESS_DENIED -> {
                            handleError(moduleName, exception, "ACCESS_DENIED")
                        }
                        // The request failed because of a network error.
                        SplitInstallErrorCode.NETWORK_ERROR -> {
                            handleError(moduleName, exception, "NETWORK_ERROR")
                        }
                        // The request contains one or more modules that have already been requested but have not yet been installed.
                        SplitInstallErrorCode.INCOMPATIBLE_WITH_EXISTING_SESSION -> {
                            handleError(moduleName, exception, "INCOMPATIBLE_WITH_EXISTING_SESSION")
                        }
                        // The service responsible for handling the request has died.
                        SplitInstallErrorCode.SERVICE_DIED -> {
                            handleError(moduleName, exception, "SERVICE_DIED")
                        }
                    }
                    moduleInstallListener.onFailure(exception)
                }
        } else {
            LOGGER.debug("Split install [$moduleName] already installed")
            moduleInstallListener.onSuccess()
        }
    }

    private fun handleError(moduleName: String, exception: Exception, error: String) {
        AbstractApplication.get().exceptionHandler.logHandledException("Split install [$moduleName] installation failed. $error", exception)
    }

    fun deferredUninstallModule(moduleName: String) {
        SplitKoinLoader.unloadKoinModule(moduleName)

        val splitInstallManager = SplitInstallManagerFactory.create(AbstractApplication.get())
        splitInstallManager.deferredUninstall(listOf(moduleName)).addOnSuccessListener {
            LOGGER.debug("Split deferred uninstall [$moduleName] requested")
            AbstractApplication.get().coreAnalyticsSender.trackSplitInstallDeferredUninstall(moduleName)
        }.addOnFailureListener {
            AbstractApplication.get().exceptionHandler.logHandledException("Split deferred uninstall [$moduleName] failed", it)
        }
    }

    inline fun <reified T> getFeatureApi(moduleName: String): T {
        val featureApi: T? = KoinHelper.getKoin().getOrNull()
        if (featureApi == null) {
            SplitKoinLoader.loadKoinModule(moduleName)
            return KoinHelper.get()
        }
        return featureApi
    }
}

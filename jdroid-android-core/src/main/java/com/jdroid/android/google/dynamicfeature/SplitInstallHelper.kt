package com.jdroid.android.google.dynamicfeature

import androidx.annotation.WorkerThread
import com.google.android.play.core.splitinstall.SplitInstallException
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.google.android.play.core.tasks.OnFailureListener
import com.google.android.play.core.tasks.OnSuccessListener
import com.jdroid.android.application.AbstractApplication
import com.jdroid.java.utils.LoggerUtils
import org.koin.core.get

object SplitInstallHelper {

    private val LOGGER = LoggerUtils.getLogger("Split")

    @WorkerThread
    fun installModuleSilently(moduleName: String, onSuccessListener: OnSuccessListener<Any>, onFailureListener: OnFailureListener) {
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
                        onFailureListener.onFailure(null)
                        splitInstallManager.unregisterListener(this)
                    } else if (state.sessionId() == mySessionId) {
                        when (state.status()) {

                            // The request has been accepted and the download should start soon.
                            SplitInstallSessionStatus.PENDING -> {
                                LOGGER.debug("Split install [$moduleName] pending")
                            }
                            // The download requires user confirmation. This is most likely due to the size of the download being larger than 10 MB.
                            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                                LOGGER.debug("Split install [$moduleName] requires user confirmation")
                                AbstractApplication.get().coreAnalyticsSender.trackSplitInstallStatus(moduleName, state)
                            }
                            // Download is in progress.
                            SplitInstallSessionStatus.DOWNLOADING -> {
                                val totalBytes = state.totalBytesToDownload()
                                val progress = state.bytesDownloaded()
                                LOGGER.debug("Split install [$moduleName] downloading $progress/$totalBytes bytes")
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
                                onSuccessListener.onSuccess(null)
                                splitInstallManager.unregisterListener(this)
                            }
                            // The request failed before the module was installed on the device.
                            SplitInstallSessionStatus.FAILED -> {
                                LOGGER.debug("Split install [$moduleName] failed: " + state.errorCode())
                                AbstractApplication.get().coreAnalyticsSender.trackSplitInstallStatus(moduleName, state)
                                onFailureListener.onFailure(null)
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
                                onFailureListener.onFailure(null)
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
                // You should also add the following listener to handle any errors
                // processing the request.
                .addOnFailureListener { exception ->
                    when ((exception as SplitInstallException).errorCode) {
                        // The request is rejected because there is at least one existing request that is currently downloading.
                        SplitInstallErrorCode.ACTIVE_SESSIONS_LIMIT_EXCEEDED -> {
                            AbstractApplication.get().exceptionHandler.logHandledException("Split install [$moduleName] installation failed. Active sessions limit exceeded")
                        }

                        // Google Play is unable to find the requested module based on the current installed version of the app, device, and user’s Google Play account.
                        SplitInstallErrorCode.MODULE_UNAVAILABLE -> {
                            // TODO
                            AbstractApplication.get().exceptionHandler.logHandledException("Split install [$moduleName] installation failed. Module unavailable")
                        }
                        // Google Play received the request, but the request is not valid.
                        SplitInstallErrorCode.INVALID_REQUEST -> {
                            // TODO
                            AbstractApplication.get().exceptionHandler.logHandledException("Split install [$moduleName] installation failed. Request invalid")
                        }
                        // A session for a given session ID was not found.
                        SplitInstallErrorCode.SESSION_NOT_FOUND -> {
                            // TODO
                            AbstractApplication.get().exceptionHandler.logHandledException("Split install [$moduleName] installation failed. Session ID not found")
                        }
                        // The Play Core Library is not supported on the current device. That is, the device is not able to download and install features on demand.
                        SplitInstallErrorCode.API_NOT_AVAILABLE -> {
                            // TODO
                            AbstractApplication.get().exceptionHandler.logHandledException("Split install [$moduleName] installation failed. Api not available")
                        }
                        // The app is unable to register the request because of insufficient permissions.
                        SplitInstallErrorCode.ACCESS_DENIED -> {
                            // TODO
                            AbstractApplication.get().exceptionHandler.logHandledException("Split install [$moduleName] installation failed. Access denied")
                        }
                        // The request failed because of a network error.
                        SplitInstallErrorCode.NETWORK_ERROR -> {
                            // TODO Display a message that requests the user to establish a network connection.
                            AbstractApplication.get().exceptionHandler.logHandledException("Split install [$moduleName] installation failed. Network error")
                        }
                        // The request contains one or more modules that have already been requested but have not yet been installed.
                        SplitInstallErrorCode.INCOMPATIBLE_WITH_EXISTING_SESSION -> {
                            // TODO
                            AbstractApplication.get().exceptionHandler.logHandledException("Split install [$moduleName] installation failed. Incompatible with existing session")
                        }
                        // The service responsible for handling the request has died.
                        SplitInstallErrorCode.SERVICE_DIED -> {
                            // TODO
                            AbstractApplication.get().exceptionHandler.logHandledException("Split install [$moduleName] installation failed. Service died")
                        }
                    }
                }
        } else {
            LOGGER.debug("Split install [$moduleName] already installed")
            onSuccessListener.onSuccess(null)
        }
    }

    fun uninstallModule(moduleName: String) {
        val splitInstallManager = SplitInstallManagerFactory.create(AbstractApplication.get())
        splitInstallManager.deferredUninstall(listOf(moduleName))
        AbstractApplication.get().coreAnalyticsSender.trackSplitInstallUninstalled(moduleName)
    }
}

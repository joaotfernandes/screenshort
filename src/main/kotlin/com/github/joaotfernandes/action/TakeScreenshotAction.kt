package com.github.joaotfernandes.action

import com.android.ddmlib.IDevice
import com.android.tools.idea.run.DeviceChooserDialog
import com.github.joaotfernandes.extension.toFile
import com.github.joaotfernandes.icon.ScreenshortIcons
import com.github.joaotfernandes.notification.Notification
import com.github.joaotfernandes.notification.NotificationManager
import com.intellij.notification.NotificationListener
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import org.jetbrains.android.sdk.AndroidSdkUtils
import org.jetbrains.android.util.AndroidUtils
import java.awt.image.BufferedImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TakeScreenshotAction : AnAction(ScreenshortIcons.SCREENSHOT_ICON) {

    companion object {
        private const val DEFAULT_DEVICE_INDEX = 0
        private const val DEFAULT_FACET_INDEX = 0
        private const val SCREENSHOT_IMAGE_FORMAT = "png"

        private const val DST_FILE_NAME_FORMAT = "%s/device-%s.%s"
        private val DATE_FORMATTER = SimpleDateFormat("yyyy-MM-dd-HHmmss")
    }

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return

        try {
            val device = getSelectedDevice(project)
            if (device != null) {
                ApplicationManager.getApplication().executeOnPooledThread {
                    val dstFile = getDstFile(project)
                    device.screenshot?.toFile(SCREENSHOT_IMAGE_FORMAT, BufferedImage.TYPE_INT_ARGB, dstFile)?.let {
                        NotificationManager.dispatchInfoNotification(
                                Notification.SCREENSHOT_SUCCESS.getBody(dstFile.absolutePath, dstFile.parent),
                                notificationListener = NotificationListener.URL_OPENING_LISTENER)
                    }
                }
            }
        } catch (throwable: Throwable) {
            // Catch all exceptions that can occur when taking a screenshot and display an error notification
            NotificationManager.dispatchErrorNotification(
                    Notification.SCREENSHOT_FAILURE.getBody(throwable.message),
                    notificationListener = NotificationListener.URL_OPENING_LISTENER)
        }
    }

    /**
     * Gets the destiny [File] where the screenshot will be saved to. The returned [File] path will be the base path
     * for the Android project with the file being named with the format 'device-yyyy-MM-dd-HHmmss.png'.
     */
    private fun getDstFile(project: Project): File {
        return File(String.format(DST_FILE_NAME_FORMAT,
                project.basePath,
                DATE_FORMATTER.format(Date()),
                SCREENSHOT_IMAGE_FORMAT))
    }

    /**
     * Get the selected device. If more than one device is connected a dialog is shown listing all connected devices
     * so a device can be selected, otherwise the only device connected is returned.
     *
     * @return selected device or null if no device was selected
     * @throws [IllegalStateException] if project configurations can't be retrieved at this time
     */
    private fun getSelectedDevice(project: Project): IDevice? {
        val debugBridge = AndroidSdkUtils.getDebugBridge(project) ?:
                throw IllegalStateException("Couldn't get debug bridge")

        return if (debugBridge.devices.size == 1) {
            debugBridge.devices[DEFAULT_DEVICE_INDEX]
        } else {
            val facet = AndroidUtils.getApplicationFacets(project).getOrNull(DEFAULT_FACET_INDEX) ?:
                    throw IllegalStateException("Couldn't get default application facet")
            val androidTarget = facet.configuration.androidTarget ?:
                    throw IllegalStateException("Couldn't get android target for default application facet")
            val deviceChooserDialog = DeviceChooserDialog(facet, androidTarget, false, null, null)

            deviceChooserDialog.show()

            if (deviceChooserDialog.exitCode == DialogWrapper.OK_EXIT_CODE) {
                deviceChooserDialog.selectedDevices[DEFAULT_DEVICE_INDEX]
            } else {
                null
            }
        }
    }
}

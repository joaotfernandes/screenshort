package com.github.joaotfernandes.notification

import com.github.joaotfernandes.icon.ScreenshortIcons
import com.intellij.notification.*
import com.intellij.openapi.application.ApplicationManager

/**
 * Singleton class to manage IntelliJ notifications. Notifications dispatched using this class will be present as a
 * balloon and will also be logged in the Event Log window
 */
object NotificationManager {

    private const val DEFAULT_NOTIFICATION_TITLE = "Screenshort"

    private val notificationGroup = NotificationGroup(DEFAULT_NOTIFICATION_TITLE,
            NotificationDisplayType.BALLOON,
            true,
            null,
            ScreenshortIcons.SCREENSHOT_ICON)

    /**
     * Dispatches an info type notification, to the main thread, that will be displayed as a balloon notification
     */
    fun dispatchInfoNotification(content: String,
                                 title: String? = DEFAULT_NOTIFICATION_TITLE,
                                 subtitle: String? = null,
                                 notificationListener: NotificationListener? = null) {
        ApplicationManager.getApplication().invokeLater({
            notificationGroup.createNotification(title,subtitle, content, NotificationType.INFORMATION,
                    notificationListener).notify(null)
        })
    }
}

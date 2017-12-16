package com.github.joaotfernandes.notification

import com.github.joaotfernandes.icon.ScreenshortIcons
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
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
        dispatchNotification(NotificationType.INFORMATION, content, title, subtitle, notificationListener)
    }

    /**
     * Dispatches an error type notification, to the main thread, that will be displayed as a balloon notification
     */
    fun dispatchErrorNotification(content: String,
                                 title: String? = DEFAULT_NOTIFICATION_TITLE,
                                 subtitle: String? = null,
                                 notificationListener: NotificationListener? = null) {
        dispatchNotification(NotificationType.ERROR, content, title, subtitle, notificationListener)
    }

    private fun dispatchNotification(notificationType: NotificationType,
                             content: String,
                                 title: String? = DEFAULT_NOTIFICATION_TITLE,
                                 subtitle: String? = null,
                                 notificationListener: NotificationListener? = null) {

        ApplicationManager.getApplication().invokeLater({
            notificationGroup.createNotification(title,subtitle, content, notificationType, notificationListener)
                    .notify(null)
        })
    }
}

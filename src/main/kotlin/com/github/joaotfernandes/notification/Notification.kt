package com.github.joaotfernandes.notification

/**
 * Notification enum class that holds all notification messages.
 *
 * Each message can be plain text or have string format placeholders.
 */
enum class Notification(private val body: String) {

    SCREENSHOT_SUCCESS("<a href='%s'>Open screenshot</a> or <a href='%s'>reveal screenshot</a>"),
    SCREENSHOT_FAILURE("Ah snap! Something went wrong. Please try again");

    /**
     * Returns the notification body formatted with [args], if any present.
     */
    fun getBody(vararg args: Any?) = String.format(body, *args)
}
package com.github.joaotfernandes

import com.intellij.openapi.util.IconLoader

/**
 * Interface to hold all relevant icons to be used in the plugin
 */
interface ScreenshortIcons {

    companion object {
        val SCREENSHOT_ICON = IconLoader.getIcon("/icons/iconScreenshot.png")
    }
}

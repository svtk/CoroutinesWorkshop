package com.kotlinconf.workshop.articles

import java.util.prefs.Preferences

private fun prefNode(): Preferences = Preferences.userRoot().node("ContributorsUI")

fun loadStoredMode(): LoadingMode {
    return LoadingMode.valueOf(prefNode().get("loading_mode", LoadingMode.BLOCKING.name))
}

fun removeStoredMode() {
    prefNode().removeNode()
}

fun saveParams(loadingMode: LoadingMode) {
    val prefNode = prefNode()
    prefNode.put("loading_mode", loadingMode.name)
    prefNode.sync()
}
package com.yernarkt.smack.vnetwork

import android.graphics.Color
import com.yernarkt.smack.BaseApplication

object UserDataService {
    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun returnAvatarColor(components: String): Int {
        val strippedColor =
            components
                .replace("[", "")
                .replace("]", "")
                .replace(",", "")

        val spllitedDoubles = strippedColor.split(" ")

        val r = (spllitedDoubles[0].toDouble() * 255).toInt()
        val g = (spllitedDoubles[1].toDouble() * 255).toInt()
        val b = (spllitedDoubles[2].toDouble() * 255).toInt()

        return Color.rgb(r, g, b)
    }

    fun logout() {
        id = ""
        avatarColor = ""
        avatarName = ""
        email = ""
        name = ""
        BaseApplication.prefs.authToken = ""
        BaseApplication.prefs.userEmail = ""
        BaseApplication.prefs.isLoggedIn = false
    }
}
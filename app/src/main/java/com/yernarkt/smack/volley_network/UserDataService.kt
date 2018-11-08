package com.yernarkt.smack.volley_network

import android.graphics.Color

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

        var r = 0
        var g = 0
        var b = 0

        r = (spllitedDoubles[0].toDouble() * 255).toInt()
        g = (spllitedDoubles[1].toDouble() * 255).toInt()
        b = (spllitedDoubles[2].toDouble() * 255).toInt()

        return Color.rgb(r, g, b)
    }

    fun logout() {
        id = ""
        avatarColor = ""
        avatarName = ""
        email = ""
        name = ""
        AuthService.authToken = ""
        AuthService.userEmail = ""
        AuthService.isLoggedIn = false
    }
}
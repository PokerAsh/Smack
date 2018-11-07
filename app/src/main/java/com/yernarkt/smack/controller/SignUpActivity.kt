package com.yernarkt.smack.controller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.yernarkt.smack.R
import com.yernarkt.smack.util.BROADCAST_USER_DATA_CHANGE
import com.yernarkt.smack.volley_network.AuthService
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*

class SignUpActivity : AppCompatActivity() {
    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        enableProgressBar(false)
    }

    fun generateUserAvatar(view: View) {
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        userAvatar = if (color == 0) "light$avatar" else "dark$avatar"
        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)

        createAvatarImageView.setImageResource(resourceId)
    }

    fun generateColorClick(view: View) {
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        createAvatarImageView.setBackgroundColor(Color.rgb(r, g, b))

        val savedR = r.toDouble() / 255
        val savedG = r.toDouble() / 255
        val savedB = r.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"
    }

    fun createUserBtnClick(view: View) {
        enableProgressBar(true)
        val userName = createUserNameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()

        if (email.isNotEmpty() || password.isNotEmpty() || userName.isNotEmpty())
            AuthService.registerUser(this, email, password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(this, email, password) { loginSuccess ->
                        if (loginSuccess) {
                            AuthService.createUser(
                                this,
                                userName,
                                email,
                                userAvatar,
                                avatarColor
                            ) { createUserSuccess ->
                                if (createUserSuccess) {
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
                                    enableProgressBar(false)
                                    finish()
                                } else {
                                    errorSnackBar(view)
                                }
                            }
                        } else {
                            errorSnackBar(view)
                        }
                    }
                } else {
                    errorSnackBar(view)
                }
            }
        else
            Snackbar.make(view, "Please enter username, email and password", Snackbar.LENGTH_LONG).show()
    }

    fun errorSnackBar(view: View) {
        enableProgressBar(false)
        Snackbar.make(view, "Something went wrong. Please try again", Snackbar.LENGTH_LONG).show()
    }

    fun enableProgressBar(enable: Boolean) {
        if (enable) {
            createPb.visibility = VISIBLE
        } else {
            createPb.visibility = GONE
        }

        createUserBtn.isEnabled = !enable
        createAvatarImageView.isEnabled = !enable
        backgroundColorBtn.isEnabled = !enable
    }
}
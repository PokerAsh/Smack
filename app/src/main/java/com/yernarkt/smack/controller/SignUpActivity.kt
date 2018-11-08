package com.yernarkt.smack.controller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import com.yernarkt.smack.R
import com.yernarkt.smack.util.BROADCAST_USER_DATA_CHANGE
import com.yernarkt.smack.vnetwork.AuthService
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
        hideSoftKeyboard()
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        userAvatar = if (color == 0) "light$avatar" else "dark$avatar"
        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)

        createAvatarImageView.setImageResource(resourceId)
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isAcceptingText) {
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    fun generateColorClick(view: View) {
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        createAvatarImageView.setBackgroundColor(Color.rgb(r, g, b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"
        hideSoftKeyboard()
    }

    fun createUserBtnClick(view: View) {
        hideSoftKeyboard()
        enableProgressBar(true)
        val userName = createUserNameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()

        if (email.isNotEmpty() || password.isNotEmpty() || userName.isNotEmpty())
            AuthService.registerUser(email, password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(email, password) { loginSuccess ->
                        if (loginSuccess) {
                            AuthService.createUser(
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

    private fun errorSnackBar(view: View) {
        enableProgressBar(false)
        Snackbar.make(view, "Something went wrong. Please try again", Snackbar.LENGTH_LONG).show()
    }

    private fun enableProgressBar(enable: Boolean) {
        if (enable) {
            createPb.visibility = VISIBLE
        } else {
            createPb.visibility = INVISIBLE
        }

        createUserBtn.isEnabled = !enable
        createAvatarImageView.isEnabled = !enable
        backgroundColorBtn.isEnabled = !enable
    }
}

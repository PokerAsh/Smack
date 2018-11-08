package com.yernarkt.smack.controller

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.yernarkt.smack.R
import com.yernarkt.smack.volley_network.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        enableProgressBar(false)
    }

    fun loginLoginBtnClick(view: View) {
        hideSoftKeyboard()
        enableProgressBar(true)
        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()

        AuthService.loginUser(this, email, password) { loginSuccess ->
            if (loginSuccess) {
                AuthService.findUserByEmail(this) { findSuccess ->
                    enableProgressBar(false)

                    if (findSuccess) {
                        finish()
                    } else {
                        Snackbar.make(view, "Could not find user", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(loginEmailText, InputMethodManager.SHOW_IMPLICIT)
        imm.showSoftInput(loginPasswordText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun loginCreateLoginBtnClick(view: View) {
        hideSoftKeyboard()
        enableProgressBar(false)
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun enableProgressBar(enable: Boolean) {
        if (enable) {
            loginProgressBar.visibility = View.VISIBLE
        } else {
            loginProgressBar.visibility = View.INVISIBLE
        }

        loginLoginBtn.isEnabled = !enable
        loginCreateLoginBtn.isEnabled = !enable
    }
}
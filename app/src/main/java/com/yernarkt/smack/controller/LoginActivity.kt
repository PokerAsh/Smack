package com.yernarkt.smack.controller

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.yernarkt.smack.R
import com.yernarkt.smack.vnetwork.AuthService
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

        if (email.isNotEmpty() || password.isNotEmpty()) {
            AuthService.loginUser(this, email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) { findSuccess ->
                        if (findSuccess) {
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
            Snackbar.make(view, "Please enter email and password", Snackbar.LENGTH_LONG).show()
        }
    }

    fun loginCreateLoginBtnClick(view: View) {
        hideSoftKeyboard()
        enableProgressBar(false)
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun errorSnackBar(view: View) {
        enableProgressBar(false)
        Snackbar.make(view, "Something went wrong. Please try again", Snackbar.LENGTH_LONG).show()
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isAcceptingText) {
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
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
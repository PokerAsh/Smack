package com.yernarkt.smack

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginBtn(view: View){

    }

    fun loginCreateLoginBtnClick(view: View){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}
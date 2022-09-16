package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import java.util.*
import kotlin.collections.ArrayList


class LogInActivity : AppCompatActivity() {
    var sign_in_section: LinearLayout? = null
    var animation_fade_in: Animation? = null

    //    Connecting to google APIs
    var google_sign_in_option: GoogleSignInOptions? = null
    var google_sign_in_client: GoogleSignInClient? = null

    //    Connecting to facebook APIs
    var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

//        Animation
        sign_in_section = findViewById(R.id.sign_in_section)
        animation_fade_in = AnimationUtils.loadAnimation(
            this@LogInActivity, R.anim.fade_in
        )

//        No Action Bar
        Utils.blackIconStatusBar(this@LogInActivity, R.color.light_background)

//        Google sign In Button and Functions
        google_sign_in_option =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        google_sign_in_client = GoogleSignIn.getClient(this@LogInActivity, google_sign_in_option!!)

//        Keep google online while logged in
        var acc_google: GoogleSignInAccount? = null
        acc_google = GoogleSignIn.getLastSignedInAccount(this@LogInActivity)
        if (acc_google != null) {
            startActivity(Intent(this@LogInActivity, GmailLogIn::class.java))
        }

        val google_signin_button = findViewById<Button>(R.id.google_signin_button)
        google_signin_button.setOnClickListener { googleSignIn() }

//        Facebook Sign In Button and Functions
        callbackManager = CallbackManager.Factory.create()

//        Keep facebook online while logged in
        var accessToken: AccessToken? = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            startActivity(Intent(this@LogInActivity, FaceBookLogIn::class.java))
            finish()
        }

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    finish()
                    startActivity(Intent(this@LogInActivity, FaceBookLogIn::class.java))
                    Toast.makeText(
                        applicationContext,
                        "You logged in with Facebook",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onCancel() {
                    // App code
                    Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onError(exception: FacebookException) {
                    // App code
                    Toast.makeText(applicationContext, "Login Error", Toast.LENGTH_SHORT)
                        .show()
                }
            })

        val facebook_signin_button = findViewById<Button>(R.id.facebook_signin_button)
        facebook_signin_button.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this@LogInActivity,
                listOf("public_profile", "email") // Permission required for Facebook
            )
        }

//        Implementing Animation
        Handler(Looper.getMainLooper()).postDelayed(
            {
                sign_in_section?.visibility = View.VISIBLE
                sign_in_section?.startAnimation(animation_fade_in)
            }, 1500
        )
    }

    //    Google Function Section
    fun googleSignIn() {
        startActivityForResult(Intent(google_sign_in_client!!.signInIntent), 1000)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data) // For Facebook
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                finish()
                startActivity(Intent(this@LogInActivity, GmailLogIn::class.java))
                var fetchmail = (GoogleSignIn.getLastSignedInAccount(this@LogInActivity))?.email
                Toast.makeText(
                    applicationContext,
                    "You logged in with \n $fetchmail",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } catch (e: ApiException) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}

package com.example.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.sign

class GmailLogIn : AppCompatActivity() {
    var google_sign_in_option: GoogleSignInOptions? = null
    var google_sign_in_client: GoogleSignInClient? = null
    var profile_google: CircleImageView? = null
    var profile_url_google: String? = null
    var name_google: TextView? = null
    var email_google: TextView? = null
    var signOut_btn_google: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gmail_log_in)

        Utils.blackIconStatusBar(this@GmailLogIn, R.color.light_background)

        name_google = findViewById(R.id.name_google)
        email_google = findViewById(R.id.email_google)
        profile_google = findViewById(R.id.profile_google)

        google_sign_in_option =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        google_sign_in_client = GoogleSignIn.getClient(this@GmailLogIn, google_sign_in_option!!)

        val acc_info = GoogleSignIn.getLastSignedInAccount(this@GmailLogIn)
        if (acc_info != null) {
            name_google?.text = acc_info.displayName
            email_google?.text = acc_info.email
            profile_url_google = (if (acc_info.photoUrl != null) {
                acc_info.photoUrl
            } else {
                "null"
            }).toString()
        }
        Picasso.get().load(profile_url_google).placeholder(R.drawable.profile).into(profile_google)

        signOut_btn_google?.setOnClickListener(View.OnClickListener { googleSignOut() })

        val google_signout_button = findViewById<Button>(R.id.signOut_btn_google)
        google_signout_button.setOnClickListener { googleSignOut() }
    }

    fun googleSignOut() {
        var fetchmail = (GoogleSignIn.getLastSignedInAccount(this@GmailLogIn))?.email
        Toast.makeText(applicationContext, "You logged out of \n $fetchmail", Toast.LENGTH_SHORT)
            .show()
        google_sign_in_client!!.signOut().addOnCompleteListener {
            finish()

            startActivity(Intent(this@GmailLogIn, MainActivity::class.java))
        }
    }
}
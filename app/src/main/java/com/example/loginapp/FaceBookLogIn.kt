package com.example.loginapp

import android.content.Intent
import android.graphics.Matrix
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class FaceBookLogIn : AppCompatActivity() {
    var profile_facebook: CircleImageView? = null
    var profile_url_facebook: String? = null
    var name_facebook: TextView? = null
    var email_facebook: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_book_log_in)

        Utils.blackIconStatusBar(this@FaceBookLogIn, R.color.light_background)

        profile_facebook = findViewById(R.id.profile_facebook)
//        profile_url_facebook = findViewById(R.id.profile_url_facebook)
        name_facebook = findViewById(R.id.name_facebook)
        email_facebook = findViewById(R.id.email_facebook)

        val signOut_btn_facebook = findViewById<Button>(R.id.signOut_btn_facebook)
        signOut_btn_facebook.setOnClickListener {
            Toast.makeText(
                this@FaceBookLogIn,
                "Facebook:\nYou have been Logged out successfully!",
                Toast.LENGTH_SHORT
            ).show()
            LoginManager.getInstance().logOut()
            startActivity(Intent(this@FaceBookLogIn, LogInActivity::class.java))
            finish()
        }

        var accessToken: AccessToken = AccessToken.getCurrentAccessToken()
        val request = GraphRequest.newMeRequest(
            accessToken
        ) { `object`, response ->
            // Application code
            name_facebook?.text = `object`.getString("name") //Getting name
            profile_url_facebook = `object`.getJSONObject("picture")
                .getJSONObject("data")
                .getString("url")
            if (profile_url_facebook != null) {
                Picasso.get().load(profile_url_facebook)
                    .placeholder(R.drawable.profile) // Getting profile pic
                    .into(profile_facebook)
            }
            if (`object`.has("email")) {
                email_facebook?.text = `object`.getString("email") // Getting email id
            } else {
                email_facebook?.text = "Email not found"
            }


        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email,link,picture.type(large)")
        request.parameters = parameters
        request.executeAsync()
    }
}
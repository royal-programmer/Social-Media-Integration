package com.example.loginapp

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Pair
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    var img_logo: ImageView? = null
    var tv_logo_section: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        No Actionbar
        Utils.blackIconStatusBar(this@MainActivity, R.color.light_background)

        img_logo = findViewById(R.id.sparks_logo)
        tv_logo_section = findViewById(R.id.tv_logo_section)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(this@MainActivity, LogInActivity::class.java)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    this@MainActivity,
                    Pair.create(img_logo, "logo"),
                    Pair.create(tv_logo_section, "logo_text")
                )
                startActivity(intent, options.toBundle())
            }, 1500
        )

    }
}
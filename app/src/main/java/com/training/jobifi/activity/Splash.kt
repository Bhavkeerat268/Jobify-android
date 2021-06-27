package com.training.jobifi.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.google.firebase.auth.FirebaseAuth
import com.training.jobify.R

class Splash : AppCompatActivity() {  /*Splash activity*/
    lateinit var txtJobify: TextView
    lateinit var logo: ImageView
    lateinit var firebaseAuth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        firebaseAuth = FirebaseAuth.getInstance()

        logo = findViewById(R.id.logo)
        txtJobify = findViewById(R.id.txtJobify)

        val animationLogo = AnimationUtils.loadAnimation(this, R.anim.lefttoright)
        logo.startAnimation(animationLogo)

        val animationText = AnimationUtils.loadAnimation(this, R.anim.fadein)
        txtJobify.startAnimation(animationText)

        Handler().postDelayed({
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, RegisterActivity::class.java)

                val options = ViewCompat.getTransitionName(logo)
                    ?.let { ActivityOptionsCompat.makeSceneTransitionAnimation(this, logo, it) }
                startActivity(intent, options?.toBundle())
            }

            finish()
        }, 3000)


    }
}






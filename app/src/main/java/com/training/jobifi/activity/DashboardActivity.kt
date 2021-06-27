package com.training.jobifi.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.training.jobifi.fragment.*
import com.training.jobify.R


class DashboardActivity : AppCompatActivity() {             /*Dashboard Activity*/
    lateinit var frame: FrameLayout
    val homeFragment= HomeFragment()
    val recommendedFragment= RecommendedFragment()
    val provideJobFragment= ProvideJobFragment()
    lateinit var bottomNavigation:BottomNavigationView
    val favouritesFragment=
        HistoryFragment()                         /*Variables decleration*/
    val profileFragment= ProfileFragment()
    lateinit var refreshButton:ImageView
    lateinit var profileimg:ImageView
    lateinit var sharedPreferences:SharedPreferences
    lateinit var settingsIcon:ImageView



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.appBlue));
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        setContentView(R.layout.activity_dashboard)


        sharedPreferences=getSharedPreferences(getString(R.string.preference_file),Context.MODE_PRIVATE)

        frame = findViewById(R.id.frame)
        bottomNavigation=findViewById(R.id.botNav)
        refreshButton=findViewById(R.id.refreshIcon)
        profileimg=findViewById(R.id.profileDash)           /*Variables intitialization*/
        settingsIcon=findViewById(R.id.settingsIcon)


        var uri:String
        uri = sharedPreferences.getString("uri", "uri").toString()            /*Setting profile image*/
        if (uri != null) {
            profileimg.setImageURI(uri!!.toUri())
        }
        else
        {
            profileimg.setImageResource(R.drawable.logonew)
        }


        settingsIcon.setOnClickListener {

          builddialog()

        }







        setCurrentFragment(homeFragment)               /*Setting current fragment*/

        bottomNavigation.setOnNavigationItemSelectedListener {      /*Implementing bottom navigation bar functionality to navigate between fragments*/
            when(it.itemId)
            {
                R.id.homeIcon->setCurrentFragment(homeFragment)
                R.id.recommended->setCurrentFragment(recommendedFragment)
                R.id.favourites->setCurrentFragment(favouritesFragment)
                R.id.profile->setCurrentFragment(profileFragment)
                R.id.provideJob->setCurrentFragment(provideJobFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }






    }



    fun builddialog()                            /*Log out functionality*/
{
    val builder = AlertDialog.Builder(this)
    //set title for alert dialog
    builder.setTitle("Log Out")
    //set message for alert dialog
    builder.setMessage("Are you sure you want to log out?")
    builder.setIcon(android.R.drawable.ic_dialog_alert)

    //performing positive action
    builder.setPositiveButton("Yes"){dialogInterface, which ->
        val firebase=FirebaseAuth.getInstance()
        firebase.signOut()
        val intent=Intent(this, RegisterActivity::class.java)
        sharedPreferences.edit().clear().apply()
        startActivity(intent)
        finish()
    }
    //performing cancel action
    builder.setNeutralButton("Cancel"){dialogInterface , which ->
        dialogInterface.dismiss()
    }
    //performing negative action
    builder.setNegativeButton("No"){dialogInterface, which ->
        dialogInterface.dismiss()
    }
    // Create the AlertDialog
    val alertDialog: AlertDialog = builder.create()
    // Set other dialog properties
    alertDialog.setCancelable(false)
    alertDialog.show()
}
private fun setCurrentFragment(fragment:Fragment) {                     /*Setting current fragment*/
    supportFragmentManager.beginTransaction().replace(R.id.frame,fragment).commit()
}
    }








package com.training.jobifi.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.FirebaseDatabase
import com.training.jobifi.adaptor.ViewPageAdaptor
import com.training.jobifi.fragment.CustomDialogFragment
import com.training.jobifi.fragment.NonSkilledWorkFragment
import com.training.jobifi.fragment.SkilledWorkFragment
import com.training.jobifi.modelClass.UserData
import com.training.jobify.R

class WorkActivity() : AppCompatActivity()      /*Work activity to hold non skilled and skilled jobs*/

{

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var sharedPreferences: SharedPreferences
    lateinit var imgLogo:ImageView
var joblist1= arrayListOf<String>()
    lateinit var btnFinish:Button



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_work)
        btnFinish=findViewById(R.id.btnfinish)
        btnFinish.visibility=View.GONE



      
        val dialog= CustomDialogFragment()
        imgLogo=findViewById(R.id.imgLogo)



        dialog.show(supportFragmentManager,"CustomDialog")

        sharedPreferences=getSharedPreferences(getString(R.string.preference_file),Context.MODE_PRIVATE)


        tabLayout=findViewById(R.id.tabLayout)
        viewPager=findViewById(R.id.viewPager)

        setupViewPager(viewPager)

        tabLayout.setupWithViewPager(viewPager)

        btnFinish.setOnClickListener {
            val reference=FirebaseDatabase.getInstance().getReference("Users")
            val fullName:String?=sharedPreferences.getString("FullName","Name")
            val mobileNo:String?=sharedPreferences.getString("MobileNo","MobileNo")
            val gender:String?=sharedPreferences.getString("gender","gender")
            val dob:String?=sharedPreferences.getString("dob","dob")

            val city:String?=sharedPreferences.getString("city","city")
            val age:String?=sharedPreferences.getString("age","0")
            val shift:String?=sharedPreferences.getString("shift","shift")
            val job1=joblist1.get(0)
            val job2=joblist1.get(1)
            val job3=joblist1.get(2)
            val job4=joblist1.get(3)
            val job5=joblist1.get(4)
            sharedPreferences.edit().putString("job1",job1).apply()
            sharedPreferences.edit().putString("job2",job2).apply()
            sharedPreferences.edit().putString("job3",job3).apply()
            sharedPreferences.edit().putString("job4",job4).apply()
            sharedPreferences.edit().putString("job5",job5).apply()


            val userData= UserData(
                fullName,
                mobileNo,
                dob,
                gender,
                city,
                shift,
                age,
                job1,
                job2,
                job3,
                job4,
                job5
            )

            if (mobileNo != null) {
                reference.child(mobileNo).setValue(userData).addOnCompleteListener {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    val intent=Intent(this,
                        DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        }
    }



    private fun setupViewPager(viewPager: ViewPager?) {
        val viewPageAdaptor: ViewPageAdaptor =
            ViewPageAdaptor(
                supportFragmentManager,
                viewPager
            )
        viewPageAdaptor.addFragment(NonSkilledWorkFragment(),"Non-Skilled")

        viewPageAdaptor.addFragment(SkilledWorkFragment(),"Skilled")
        this.viewPager.adapter=viewPageAdaptor


    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }




}

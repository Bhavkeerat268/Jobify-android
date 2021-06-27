package com.training.jobifi.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.training.jobifi.fragment.ProfileFragment.MyClass.Companion.PICK_IMAGE
import com.training.jobifi.activity.DashboardActivity
import com.training.jobify.R


class ProfileFragment : Fragment() {                /* User Profile Fragment*/

lateinit var pickimg:Button
    lateinit var mActivity: DashboardActivity
    lateinit var username:TextView
    lateinit var city:TextView
    lateinit var age:TextView
    lateinit var mobileNo:TextView
    lateinit var totalJobs:TextView
    lateinit var jobPref:TextView
    var joblist= arrayListOf<String>()
    var state="non_refreshed"

    class MyClass {
        companion object {
            val PICK_IMAGE = 1
            val PERM_CODE=2


        }
    }

lateinit var sharedPreferences:SharedPreferences
    lateinit var imgview:ImageView
    var imageuri: Uri?=null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DashboardActivity) {
            mActivity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences=requireActivity().getSharedPreferences(getString(R.string.preference_file),Context.MODE_PRIVATE)
        val mobile=sharedPreferences.getString("MobileNo","Mobile")
        imgview=view.findViewById(R.id.imgview)
        pickimg=view.findViewById(R.id.pickimg)
        username=view.findViewById(R.id.namecolumn)
        city=view.findViewById(R.id.citycolumn)
        age=view.findViewById(R.id.agecolumn)
        mobileNo=view.findViewById(R.id.mobilecolumn)
        totalJobs=view.findViewById(R.id.totaljobs)
        jobPref=view.findViewById(R.id.jobspref)




        if(state=="non_refreshed")
        {
       getdata()                    /*If state=non_refreshed then we will fetch data */

        }


/*Firebase to fetch user profile data */

        var firebaseins= FirebaseDatabase.getInstance().getReference("UserHistory").child("$mobile").orderByChild("sortId")
        if (firebaseins != null) {
            firebaseins.addListenerForSingleValueEvent(object :ValueEventListener
            {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val n=snapshot.children.count()
                   sharedPreferences.edit().putString("TotalJobs",n.toString()).apply()
                    totalJobs.text=sharedPreferences.getString("TotalJobs","No Jobs")
                    for (snap:DataSnapshot in snapshot.children)
                    {
                         snap.child("hisJobName").getValue(String::class.java)
                        sharedPreferences.edit().putString("LastJob",snap.child("hisJobName").getValue(String::class.java)).apply()
                        jobPref.text=sharedPreferences.getString("LastJob","No Jobs")
                    }


                }

            })
        }
        if(state=="refreshed")
        {
            username.text=sharedPreferences.getString("ProfileName","profName")
            city.text=sharedPreferences.getString("ProfileCity","profName")
            age.text=sharedPreferences.getString("ProfileAge","profName")        /*If state=refreshed then we will not fetch data again unless refresh button is pressed*/
            mobileNo.text=sharedPreferences.getString("ProfileMobile","profName")
            totalJobs.text=sharedPreferences.getString("TotalJobs","No Jobs")
            jobPref.text=sharedPreferences.getString("LastJob","No Jobs")
        }





        pickimg.setOnClickListener {   /*Pick user prfile image from gallery*/
            val galleryIntent=Intent()
            galleryIntent.setType("image/*")
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(galleryIntent,"Select Picture"),
                MyClass.PICK_IMAGE)



        }




mActivity.refreshButton.setOnClickListener {        /*Refresh button to fetch data*/
    getdata()

}


        loadImage()







        return view
    }
    fun getdata()                                              /*Fetch data from firebase*/
    {
        val mobile=sharedPreferences.getString("MobileNo","Mobile")

        var firebaseDatains= mobile?.let {
            FirebaseDatabase.getInstance().getReference("Users").child(
                it
            )
        }
        if (firebaseDatains != null) {
            firebaseDatains.addListenerForSingleValueEvent(object :ValueEventListener
            {

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(activity as Context, error.message, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    state="refreshed"
                    sharedPreferences.edit().putString("ProfileName",snapshot.child("fullName").getValue(String::class.java)).apply()
                    sharedPreferences.edit().putString("ProfileCity",snapshot.child("city").getValue(String::class.java)).apply()
                    sharedPreferences.edit().putString("ProfileAge","${snapshot.child("age").getValue(String::class.java)} years").apply()
                    sharedPreferences.edit().putString("ProfileMobile",snapshot.child("mobileNo").getValue(String::class.java)!!.substring(3)).apply()

                    username.text=sharedPreferences.getString("ProfileName","profName")
                    city.text=sharedPreferences.getString("ProfileCity","profName")
                    age.text=sharedPreferences.getString("ProfileAge","profName")
                    mobileNo.text=sharedPreferences.getString("ProfileMobile","profName")

                }

            })
        }
    }
    public fun checkPermissionForReadExtertalStorage():Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = context?.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }



    /*Setting profile image*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {
            if (data != null) {
                imageuri=data.data
               imgview.setImageURI(imageuri)
                mActivity.profileimg.setImageURI(imageuri)
                sharedPreferences.edit().putString("uri",imageuri.toString()).apply()


            }

        }
    }


    /*Loading profile image*/
    fun loadImage()
    {
        if (checkPermissionForReadExtertalStorage()==false)
        {
requestPermissionForReadExtertalStorage()
        }
        var uri=sharedPreferences.getString("uri","Uri")
        if (uri != null) {
            imgview.setImageURI(uri.toUri())
        }
        else
        {
            imgview.setImageResource(R.drawable.userprofile)
        }

    }

    @Throws(Exception::class)
    fun requestPermissionForReadExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                MyClass.PERM_CODE
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }




}


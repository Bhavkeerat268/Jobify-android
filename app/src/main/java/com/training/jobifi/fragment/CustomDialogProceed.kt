package com.training.jobifi.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.FirebaseDatabase
import com.training.jobifi.modelClass.UserHistoryModel
import com.training.jobifi.activity.BookSuccess
import com.training.jobifi.activity.DetailsActivity
import com.training.jobify.R
import java.text.SimpleDateFormat
import java.util.*


class CustomDialogProceed : DialogFragment() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var mActivity: DetailsActivity


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DetailsActivity) {
            mActivity = context
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_custom_dialog_proceed, container, false)

        val fireIns = FirebaseDatabase.getInstance()            /*Get firebase instance*/
        sharedPreferences = mActivity.getSharedPreferences(
            getString(R.string.preference_file),
            Context.MODE_PRIVATE
        )                                                                      /*Get shared preferences*/


        var user = sharedPreferences.getString("FullName", "User")
        var jobId = mActivity.intent.getStringExtra("id")             /*Variables Initializtion*/
        val btnProceed = view.findViewById<Button>(R.id.btnProceed)
        val btnNo = view.findViewById<Button>(R.id.btnNo)


        dialog?.getWindow()?.setBackgroundDrawableResource(R.drawable.dialog_rounded_background)     /*Dialog box to Book a job slot*/


        btnNo.setOnClickListener {
            dialog?.dismiss()
        }


        btnProceed.setOnClickListener {
            val number = sharedPreferences.getString("jobProvNumber", "wrong")


            val sdf = SimpleDateFormat("HH:mm:ss")
            val currentDateandTime: String = sdf.format(Date())
            var bookId = "${sharedPreferences.getString("MobileNo", "MobileNo")}$currentDateandTime"
            var jobName = mActivity.intent.getStringExtra("jobName")
            var jobUser = mActivity.intent.getStringExtra("jobUser")
            var jobTime = mActivity.txtJobTime.text.toString()
            var jobPay = mActivity.txtJobPay.text.toString()
            var status: String = "Pending"


            val BookHis = jobId?.let { it1 ->
                UserHistoryModel(
                    bookId, jobName, jobUser, jobTime, jobPay,
                    it1, status
                )
            }

            fireIns.getReference("UserHistory")
                .child("${sharedPreferences.getString("MobileNo", "MobileNo")}").child(bookId)
                .setValue(BookHis).addOnCompleteListener {    /*Pushing data to firebase*/

                    val intent = Intent(activity as Context, BookSuccess::class.java)



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(
                            activity as Context,
                            android.Manifest.permission.SEND_SMS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {                                                /*Sending a sms to the person whose job slot have been booked*/
                        var smsNumber = number?.subSequence(3, 13)?.trim().toString()
                        var sms =
                            "Jobify Update:\n Your Job :$jobName(jobid:$jobId) has been booked by $user with phone number ${sharedPreferences.getString(
                                "MobileNo",
                                "MobileNo"
                            )}"
                        var smsManager: SmsManager = SmsManager.getDefault()
                        smsManager.sendTextMessage(smsNumber, null, sms, null, null)
                    } else {
                        requestPermissions(arrayOf<String>(android.Manifest.permission.SEND_SMS), 1)
                        var smsNumber = number?.subSequence(3, 13)?.trim().toString()
                        var sms =
                            "Jobify Update:\n Your Job :$jobName(jobid:$jobId) has been booked by $user with phone number ${sharedPreferences.getString(
                                "MobileNo",
                                "MobileNo"
                            )}"
                        var smsManager: SmsManager = SmsManager.getDefault()
                        smsManager.sendTextMessage(smsNumber, null, sms, null, null)
                    }
                }




                if (number != null) {                                         /*Pushing data to firebase*/
                    var user = sharedPreferences.getString("FullName", "User")
                    var userMobile = sharedPreferences.getString("MobileNo", "MobileNo")
                    fireIns.getReference("UserPostedJob/$number")
                        .child("${mActivity.intent.getStringExtra("id")}").child("book")
                        .setValue("Yes")
                    fireIns.getReference("UserPostedJob/$number")
                        .child("${mActivity.intent.getStringExtra("id")}").child("bookerNumber")
                        .setValue(userMobile)
                    fireIns.getReference("UserPostedJob/$number")
                        .child("${mActivity.intent.getStringExtra("id")}").child("bookerJobId")
                        .setValue(bookId)
                    fireIns.getReference("UserPostedJob/$number")
                        .child("${mActivity.intent.getStringExtra("id")}").child("bookedBy")
                        .setValue(user)

                    fireIns.getReference("JobList")
                        .child("${mActivity.intent.getStringExtra("id")}").child("book")
                        .setValue("Yes")
                    fireIns.getReference("JobList")
                        .child("${mActivity.intent.getStringExtra("id")}").child("bookerNumber")
                        .setValue(userMobile)
                    fireIns.getReference("JobList")
                        .child("${mActivity.intent.getStringExtra("id")}").child("bookerJobId")
                        .setValue(bookId)
                    fireIns.getReference("JobList")
                        .child("${mActivity.intent.getStringExtra("id")}").child("bookedBy")
                        .setValue(user)
                }


                startActivity(intent)
                mActivity.finish()


            }


        }
        return view
    }
}
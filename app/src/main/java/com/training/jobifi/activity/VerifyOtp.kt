package com.training.jobifi.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.chaos.view.PinView
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.training.jobify.R
import java.util.concurrent.TimeUnit

class VerifyOtp : AppCompatActivity() {             /*Verify orp class*/
    lateinit var otpView: PinView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var numDesc:TextView


    lateinit var btnVerify: Button
//    lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken
    lateinit var callBacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var storedId:String
    var TAG:String="MAIN_TAG"
    lateinit var firebaseAuth: FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w: Window = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        setContentView(R.layout.activity_verify_otp)
        numDesc=findViewById(R.id.numDesc)


        otpView = findViewById(R.id.otpView)
        btnVerify = findViewById(R.id.btnVerify)
        firebaseAuth= FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        val phone=sharedPreferences.getString("MobileNo","mobile")
        numDesc.text="Please enter the otp sent to $phone"


        callBacks=object : PhoneAuthProvider.OnVerificationStateChangedCallbacks()  /*Handling callbacks*/
        {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                val code= p0.smsCode      //AUTOVERIFICATION
                otpView.setText(code)
                veriFyPhoneNoWithCode(storedId,code)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@VerifyOtp, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(fstoredId: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(fstoredId, p1)
                Log.d(TAG,"onCodeSent  $fstoredId")
                storedId=fstoredId

                Toast.makeText(this@VerifyOtp, "Code sent", Toast.LENGTH_SHORT).show()

            }

        }
        startPhoneNoVerification(phone)
        btnVerify.setOnClickListener {
            val code=otpView.text.toString()
            veriFyPhoneNoWithCode(storedId,code)
        }





    }

    private fun startPhoneNoVerification(phone: String?) {   /*Send otp to phone number for authentication*/

        val options=PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callBacks)
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)


    }

    private fun veriFyPhoneNoWithCode(storedId: String, code: String)       /*Verify otp*/
    {
        Toast.makeText(this, "Verifying code", Toast.LENGTH_SHORT).show()
        val credential=PhoneAuthProvider.getCredential(storedId,code)
        signWithPhoneAuthCredential(credential)
    }

    private fun signWithPhoneAuthCredential(credential: PhoneAuthCredential) {  /*Sign in user*/


        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this) {task->
            if(task.isSuccessful)
            {
                val phone=sharedPreferences.getString("MobileNo","mobile")
                val ref= FirebaseDatabase.getInstance().getReference("Users").child("$phone")
                ref.addListenerForSingleValueEvent(object :ValueEventListener
                {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists())
                        {
                            Toast.makeText(this@VerifyOtp, "Exists", Toast.LENGTH_SHORT).show()
                            val intent=Intent(this@VerifyOtp,
                                DashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else
                        {  Toast.makeText(this@VerifyOtp, "Does not Exists", Toast.LENGTH_SHORT).show()
                            val intent=Intent(this@VerifyOtp,
                                BasicInfo::class.java)
                            startActivity(intent)
                            finish()

                        }
                    }

                })



            }
            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(this, "Verification not completed", Toast.LENGTH_SHORT)
                        .show()
            }
        }

        }


    }







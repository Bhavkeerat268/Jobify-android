package com.training.jobifi.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.training.jobify.R
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {  /*Activity to sign up user*/

    lateinit var btnNext: Button
    lateinit var etName: TextInputLayout
    lateinit var etMobileNo: TextInputLayout
    lateinit var sharedPreferences: SharedPreferences
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var reglayout: LinearLayout


    @SuppressLint("CommitPrefEdits", "StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()
        reglayout = findViewById(R.id.regLayout)





        btnNext = findViewById(R.id.btnNext)
        etName = findViewById(R.id.etName)
        etMobileNo = findViewById(R.id.etMobileNo)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)





        etName.editText?.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                reglayout.translationY = -300f
            }

        }



        btnNext.setOnClickListener {
            if (checkName(etName.editText?.text.toString()) && checkNumber(etMobileNo.editText?.text.toString())) {


                val intent = Intent(this, VerifyOtp::class.java)
                val trimmedEt = etName.editText?.text.toString().trim()
                val name = trimmedEt.split(" ")
                val nameFinal = name[0]
                val phone = "+91${etMobileNo.editText?.text.toString()}"
                sharedPreferences.edit().putString("Name", nameFinal).apply()



                sharedPreferences.edit().putString("MobileNo", phone).apply()
                sharedPreferences.edit().putString("FullName", trimmedEt).apply()



                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


            } else {
                Toast.makeText(this, "Please fill details properly", Toast.LENGTH_SHORT).show()
            }


        }

    }


    private fun checkName(name: String): Boolean {
        when {
            name.isEmpty() -> {
                etName.editText?.error = "Name cannot be empty"
                return false

            }
            (name.length < 3) -> {
                etName.editText?.error = "Name must contain atleast 3 characters"
                return false

            }
        }
        return true


    }

    private fun checkNumber(mobileNo: String): Boolean {  /*Phone format validation*/
        val check = Pattern.compile("^(\\+91[\\-\\s]?)?[6789]\\d{9}\$")
        if (check.matcher(mobileNo).matches()) {
            return true
        }
        return false
    }


}









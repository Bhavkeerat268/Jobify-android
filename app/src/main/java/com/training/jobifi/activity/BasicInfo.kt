package com.training.jobifi.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.training.jobify.R
import java.text.SimpleDateFormat
import java.util.*/**/

class BasicInfo : AppCompatActivity() {

    lateinit var tilGender: TextInputLayout                      /*Gender Input*/
    lateinit var actvGender: AutoCompleteTextView
    lateinit var tilCity: TextInputLayout                       /*City Input*/
    lateinit var actvCity: AutoCompleteTextView
    lateinit var tilShift: TextInputLayout
    lateinit var actvShift: AutoCompleteTextView                /*Timings shift Input*/
    lateinit var sharedPreferences: SharedPreferences
    lateinit var txtWelcome: TextView
    lateinit var etDOB: TextInputLayout                        /*Date of birth  Input*/
    lateinit var firebaseAuth: FirebaseAuth                   /*Firebase authentication*/
    lateinit var btnNext: Button                               /*Button*/
    lateinit var genderString: String
    lateinit var dateString: String
    lateinit var cityString: String
    lateinit var shiftString: String
    var gender = listOf<String>("Male", "Female")
    val cityList = listOf<String>(
        "Abohar",
        "Amritsar",
        "Barnala",
        "Batala",
        "Bathinda",
        "Faridkot",
        "Firozpur",
        "Hoshiarpur",
        "Jalandhar",
        "Kapurthala",
        "Khanna",
        "Ludhiana",
        "Malerkotla",
        "Moga",
        "Mohali",
        "Muktsar",
        "Pathankot",
        "Patiala",
        "Phagwara",
        "Rajpura",
        "Sunam"
    )                                /*List of cities*/
    val shiftList = listOf<String>(
        "Morning(8:00-11:59)",
        "Afternoon(12:00-15:59)",
        "Evening(16:00-18:59)",
        "Night(19:00:22:00)"
    )                                                              /*List of Shifts*/
    val calender = Calendar.getInstance()                 /*Calender to select DOB*/
    val year = calender.get(Calendar.YEAR)
    val month = calender.get(Calendar.MONTH)
    val day = calender.get(Calendar.DAY_OF_MONTH)
    var age: Int = 0


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_basic_info)

        firebaseAuth = FirebaseAuth.getInstance()
        btnNext = findViewById(R.id.btnNext)
        tilGender = findViewById(R.id.tilGender)
        actvGender = findViewById(R.id.actvGender)
        txtWelcome = findViewById(R.id.txtWelcome)
        etDOB = findViewById(R.id.etDOB)                   /*Variables intialization*/
        tilCity = findViewById(R.id.tilCity)
        actvCity = findViewById(R.id.actvCity)
        tilShift = findViewById(R.id.tilShift)
        actvShift = findViewById(R.id.actvShift)
        actvGender.inputType = InputType.TYPE_NULL
        actvCity.inputType = InputType.TYPE_NULL
        actvShift.inputType = InputType.TYPE_NULL
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)          /*shared preference to get username from previous activity*/

        txtWelcome.text = "Hi, ${sharedPreferences.getString("Name", "User")}"


        val genderArrayAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, gender)
        actvGender.setAdapter(genderArrayAdapter)                                /*Adaptor to implement autocompletetextview*/

        val cityAdaptor =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, cityList)

        actvCity.setAdapter(cityAdaptor)                                       /*Adaptor to implement autocompletetextview*/


        val professionAdaptor =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, shiftList)
        actvShift.setAdapter(professionAdaptor)                              /*Adaptor to implement autocompletetextview*/

        etDOB.editText?.transformIntoDatePicker(this, "dd/MM/yyyy")
        etDOB.editText?.transformIntoDatePicker(this, "dd/MM/yyyy", Date())




        btnNext.setOnClickListener {                /*Button to proceed to next activity*/
            genderString = tilGender.editText?.text.toString()
            cityString = tilCity.editText?.text.toString()
            shiftString = tilShift.editText?.text.toString()
            if (genderString != null && cityString != null && dateString != null && shiftString != null) {
                val intent = Intent(this, WorkActivity::class.java)
                sharedPreferences.edit().putString("gender", genderString).apply()
                sharedPreferences.edit().putString("city", cityString).apply()
                sharedPreferences.edit().putString("dob", dateString).apply()
                sharedPreferences.edit().putString("age", age.toString()).apply()
                sharedPreferences.edit().putString("shift", shiftString).apply()

                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please fill all details ", Toast.LENGTH_SHORT).show()
            }

        }


    }

    @RequiresApi(Build.VERSION_CODES.N)

    fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {  /*Date picker implementation*/
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false
        val cyear = Calendar.getInstance().get(Calendar.YEAR)
        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                age = cyear - year
                print("Age is $age")
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))
                dateString = sdf.format((myCalendar.time)).replace("/", "")

            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }
}
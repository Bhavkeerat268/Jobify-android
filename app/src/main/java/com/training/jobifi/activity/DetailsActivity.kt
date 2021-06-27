package com.training.jobifi.activity


import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.training.jobifi.fragment.CustomDialogProceed
import com.training.jobify.R
import org.json.JSONObject

class DetailsActivity :
    AppCompatActivity() {          /*Activity to show details about a job to book a slot*/

    lateinit var txtJobName: TextView
    lateinit var txtJobUserName: TextView
    lateinit var txtJobLocation: TextView
    lateinit var txtJobGender: TextView
    lateinit var txtJobTime: TextView                        /*Variables Declaration*/
    lateinit var txtJobAge: TextView
    lateinit var txtJobPay: TextView
    lateinit var imgJob: ImageView
    lateinit var btndashboard: Button
    lateinit var btnBookSlot: Button
    lateinit var progressImg: ProgressBar
    lateinit var jobProvNumer: String
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        progressImg = findViewById(R.id.progressImg)                         /*Variables Initialization*/
        progressImg.visibility = View.VISIBLE
        var jobName = intent.getStringExtra("jobName")
        var jobUser = intent.getStringExtra("jobUser")
        var jobLocation = intent.getStringExtra("jobLocation")
        var jobId: String? = intent.getStringExtra("id")
        var jobTime: String
        var jobGender: String
        var jobAge: String
        var jobPay: String
        var timec: String
        var agec: String



        imgJob = findViewById(R.id.imgJob)

        txtJobName = findViewById(R.id.jobName)
        txtJobUserName = findViewById(R.id.jobUser)
        txtJobLocation = findViewById(R.id.jobLocation)
        txtJobGender = findViewById(R.id.gendercontainer)
        txtJobTime = findViewById(R.id.timecontainer)
        txtJobAge = findViewById(R.id.agecontainer)
        txtJobPay = findViewById(R.id.paycontainer)
        btndashboard = findViewById(R.id.dashboard)
        btnBookSlot = findViewById(R.id.bookSlot)



        val queue = Volley.newRequestQueue(this)         /*Preparing a request queue to send an API request*/

        val url = "https://jobify11.herokuapp.com/rec/$jobId"                /*API to fetch job details*/
        val jsonObjectRequest = @RequiresApi(Build.VERSION_CODES.KITKAT)
        object : JsonObjectRequest(Method.GET, url, null,
            Response.Listener {
                var jsonobject = it.getString("item_data")
                var reader = JSONObject(jsonobject)
                jobGender = reader.getString("gender")              /*Getting json response and initializing variables to put in a data class*/
                jobTime = reader.getString("time")
                jobAge = reader.getString("age")
                jobPay = reader.getString("pay")
                jobProvNumer = reader.getString("number")
                sharedPreferences.edit().putString("jobProvNumber", jobProvNumer).apply()
                txtJobName.text = jobName
                txtJobUserName.text = jobUser
                txtJobLocation.text = jobLocation



                if (jobTime != null) {                      /*Time formatting*/
                    if (jobTime.substring(0, 2).toInt() > 12) {
                        timec = "0${(jobTime.substring(0, 2).toInt() - 12)}:${jobTime.substring(
                            3,
                            5
                        )} PM"
                        txtJobTime.text = timec
                    } else {
                        timec = "$jobTime AM"
                        txtJobTime.text = timec
                    }

                }
                when {
                    jobAge == "slotA" -> {              /*Setting slots according to age groups*/
                        agec = "20-30 years"
                        txtJobAge.text = agec
                    }
                    jobAge == "slotB" -> {
                        agec = "30-40 years"
                        txtJobAge.text = agec
                    }
                    jobAge == "slotC" -> {
                        agec = "40-50 years"
                        txtJobAge.text = agec
                    }
                }

                txtJobGender.text = jobGender

                txtJobPay.text = "Rs $jobPay"

                when {                             /*Setting images for jobs*/
                    jobName == "CarWashing" -> setupImage(R.drawable.carwashimg)
                    jobName == "Delivery" -> setupImage(R.drawable.delivery)
                    jobName == "Packing" -> setupImage(R.drawable.packing)
                    jobName == "Sweeping" -> setupImage(R.drawable.sweeping)
                    jobName == "Salesman" -> setupImage(R.drawable.salesman)
                    jobName == "Promotions" -> setupImage(R.drawable.promotion)
                    jobName == "Helper" -> setupImage(R.drawable.helper)
                    jobName == "ProductsSampling" -> setupImage(R.drawable.product)
                    jobName == "BrochureDistribution" -> setupImage(R.drawable.brouchers)
                    jobName == "BrandingOfProducts" -> setupImage(R.drawable.branding)
                    jobName == "Electrician" -> setupImage(R.drawable.electrician)
                    jobName == "Labour" -> setupImage(R.drawable.labour)
                    jobName == "Tuitions" -> setupImage(R.drawable.tutions)
                    jobName == "Plumbing" -> setupImage(R.drawable.plumbing)
                    jobName == "Welding" -> setupImage(R.drawable.welding)
                    jobName == "Carpentering" -> setupImage(R.drawable.carpenter)
                    jobName == "Painter" -> setupImage(R.drawable.painterr)

                }


            }, Response.ErrorListener {
                print("Error is $it")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                return headers
            }


        }
        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        queue.add(jsonObjectRequest)               /*Sending api request*/
        val dialog = CustomDialogProceed()








        btndashboard.setOnClickListener {

            finish()
        }
        btnBookSlot.setOnClickListener {

            dialog.show(supportFragmentManager, "CustomDial")


        }

    }

    fun setupImage(drawable: Int) {                              /*Function to set image in the image view*/
        Glide.with(this)
            .load(drawable)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgJob)
        progressImg.visibility = View.GONE
    }
}
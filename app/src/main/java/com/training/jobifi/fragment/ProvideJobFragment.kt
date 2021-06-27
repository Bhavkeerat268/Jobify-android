package com.training.jobifi.fragment

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.training.jobifi.modelClass.PostedWorkItem
import com.training.jobifi.adaptor.ProvideJobAdaptor
import com.training.jobifi.modelClass.ProvideWorkItem
import com.training.jobifi.activity.DashboardActivity
import com.training.jobify.R
import java.text.SimpleDateFormat
import java.util.*


class ProvideJobFragment : Fragment() {                 /*Fragmento provide a job */

    lateinit var mActivity: DashboardActivity
    lateinit var postAJob: Button
    lateinit var tiljobLocation: TextInputLayout
    lateinit var actvjobLocation: AutoCompleteTextView
    lateinit var tilJobName: TextInputLayout
    lateinit var actvJobName: AutoCompleteTextView
    lateinit var tilJobPay: TextInputLayout
    lateinit var tilGender: TextInputLayout
    lateinit var actvGender: AutoCompleteTextView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var btnPost: Button
    lateinit var tilTimings: TextInputLayout
    lateinit var tilAge: TextInputLayout
    lateinit var actvAge: AutoCompleteTextView
    lateinit var btncheckPrev: Button
    lateinit var layoutPostJob: LinearLayout
    lateinit var recyclerCheckPrev: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var provideJobAdaptor: ProvideJobAdaptor
    var status: String = ""
    var pay: String = ""
    var time: String = ""
    var location: String = ""
    var bookedBy: String = ""
    var jobId: String = ""
    var bookerJobId: String = ""
    var bookerNumer: String = ""
    var jobName: String = ""
    var confirmed = ""
    var provideArray = arrayListOf<PostedWorkItem>()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DashboardActivity) {
            mActivity = context
        }
    }

    var Jobgender = listOf<String>("Male", "Female")
    var ageList = listOf<String>("20-30 years", "30-40 years", "40-50 years")
    val JobcityList = listOf<String>(
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
    )
    var jobList = listOf<String>(
        "CarWashing",
        "Delivery",
        "Packing",
        "Sweeping",
        "Salesman",
        "Promotions",
        "Helper",
        "ProductsSampling",
        "BrochureDistribution",
        "BrandingOfproducts",
        "Labour",
        "Tuitions",
        "Plumbing",
        "Welding",
        "Electrician",
        "Carpentering",
        "Painter"
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_provide_job, container, false)







        recyclerCheckPrev = view.findViewById(R.id.recyclerPostJob)
        layoutManager = LinearLayoutManager(activity as Context)
        recyclerCheckPrev.visibility = View.GONE







        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file),
            Context.MODE_PRIVATE
        )
        btncheckPrev = view.findViewById(R.id.btnCheckPrev)


        var firebase = FirebaseDatabase.getInstance()
        tiljobLocation = view.findViewById(R.id.tilLocation)
        actvjobLocation = view.findViewById(R.id.actvLocation)
        tilJobName = view.findViewById(R.id.tilJobName)
        actvJobName = view.findViewById(R.id.actvJobName)
        tilJobPay = view.findViewById(R.id.tilJobpay)
        tilGender = view.findViewById(R.id.tilGender)
        actvGender = view.findViewById(R.id.actvGender)
        btnPost = view.findViewById(R.id.btnPost)
        tilTimings = view.findViewById(R.id.tilTimings)
        tilAge = view.findViewById(R.id.tilAge)
        actvAge = view.findViewById(R.id.actvAge)
        postAJob = view.findViewById(R.id.btnPostAJob)
        layoutPostJob = view.findViewById(R.id.layoutPostJob)
        layoutPostJob.visibility = View.GONE

        actvJobName.inputType = InputType.TYPE_NULL
        actvjobLocation.inputType = InputType.TYPE_NULL
        actvAge.inputType = InputType.TYPE_NULL
        actvGender.inputType = InputType.TYPE_NULL

        postAJob.setOnClickListener {
            if (layoutPostJob.visibility == View.VISIBLE) {
                layoutPostJob.visibility = View.GONE
                postAJob.text = "Post a Job"
            } else if (layoutPostJob.visibility == View.GONE) {
                layoutPostJob.visibility = View.VISIBLE
                postAJob.text = "Cancel"
            }
        }

        var calendar: Calendar
        var CalendarHour: Int
        var CalendarMinute: Int

        val genderArrayAdapter = ArrayAdapter(
            activity as Context,
            R.layout.support_simple_spinner_dropdown_item,
            Jobgender
        )
        actvGender.setAdapter(genderArrayAdapter)

        val cityAdaptor = ArrayAdapter(
            activity as Context,
            R.layout.support_simple_spinner_dropdown_item,
            JobcityList
        )

        actvjobLocation.setAdapter(cityAdaptor)

        val jobListAdaptor = ArrayAdapter(
            activity as Context,
            R.layout.support_simple_spinner_dropdown_item,
            jobList
        )
        actvJobName.setAdapter(jobListAdaptor)

        val ageAdaptor = ArrayAdapter(
            activity as Context,
            R.layout.support_simple_spinner_dropdown_item,
            ageList
        )
        actvAge.setAdapter(ageAdaptor)


        tilTimings.editText?.setOnClickListener {
            calendar = Calendar.getInstance()
            CalendarHour = calendar.get(Calendar.HOUR)
            CalendarMinute = calendar.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(
                activity as Context,
                TimePickerDialog.OnTimeSetListener(function = { view, h, m ->

                    var hour = h
                    var minutes = m
                    var timeSet = ""
                    if (hour > 12) {
                        hour -= 12
                        timeSet = "PM"
                    } else if (hour === 0) {
                        hour += 12
                        timeSet = "AM"
                    } else if (hour === 12) {
                        timeSet = "PM"
                    } else {
                        timeSet = "AM"
                    }

                    var min: String? = ""
                    if (minutes < 10) min = "0$minutes" else min = java.lang.String.valueOf(minutes)

                    // Append in a StringBuilder

                    // Append in a StringBuilder
                    if (hour.toString().count() == 1) {
                        val aTime: String = StringBuilder().append("0$hour").append(':')
                            .append(min.toString()).append(" ").append(timeSet).toString()
                        tilTimings.editText?.setText(aTime)
                    } else {
                        val aTime: String = StringBuilder().append(hour.toString()).append(':')
                            .append(min.toString()).append(" ").append(timeSet).toString()
                        tilTimings.editText?.setText(aTime)
                    }


                }),
                CalendarHour,
                CalendarMinute,
                false
            )

            tpd.show()

        }










        btnPost.setOnClickListener {
            val jobName = tilJobName.editText?.text.toString()
            val locationString = tiljobLocation.editText?.text.toString()
            val genderString = tilGender.editText?.text.toString()
            val jobpay = tilJobPay.editText?.text.toString()
            val jobAge = tilAge.editText?.text.toString()
            val jobTime = tilTimings.editText?.text.toString()


            val jobProvideName = sharedPreferences.getString("FullName", "Name")

            if (tilJobName.editText?.text?.length != 0 && tiljobLocation.editText?.text?.length != 0 && tilGender.editText?.text?.length != 0 && tilJobPay.editText?.text?.length != 0 && tilAge.editText?.text?.length != 0 && tilTimings.editText?.text?.length != 0) {
                var timeString = " "
                var shift = " "
                var slot = " "
                if (jobAge == "20-30 years") {
                    slot = "slotA"
                }
                if (jobAge == "30-40 years") {
                    slot = "slotB"
                }
                if (jobAge == "40-50 years") {
                    slot = "slotC"
                }






                if (jobTime.subSequence(6, 8) == "AM") {
                    if (jobTime.subSequence(0, 2).toString().toInt() < 8) {
                        timeString = " "
                    } else {
                        timeString = "${jobTime.subSequence(0, 2)}:${jobTime.subSequence(3, 5)}"

                        if (timeString.subSequence(0, 2).toString()
                                .toInt() in 8..11 && timeString.subSequence(3, 5).toString()
                                .toInt() in 0..59
                        ) {
                            shift = "Morning(8:00-11:59)"
                        }

                    }

                } else if (jobTime.subSequence(6, 8) == "PM") {
                    if (jobTime.subSequence(0, 2).toString().toInt() >= 10 && jobTime.subSequence(
                            3,
                            5
                        ).toString().toInt() > 0
                    ) {
                        timeString = " "
                    } else {
                        timeString = "${jobTime.subSequence(0, 2).toString()
                            .toInt() + 12}:${jobTime.subSequence(3, 5)}"
                        if (timeString.subSequence(0, 2).toString()
                                .toInt() in 12..15 && timeString.subSequence(3, 5).toString()
                                .toInt() in 0..59
                        ) {
                            shift = "Afternoon(12:00-15:59)"
                        }
                        if (timeString.subSequence(0, 2).toString()
                                .toInt() in 16..18 && timeString.subSequence(3, 5).toString()
                                .toInt() in 0..59
                        ) {
                            shift = "Evening(16:00-18:59)-"
                        }
                        if ((timeString.subSequence(0, 2).toString()
                                .toInt() in 19..21 && timeString.subSequence(3, 5).toString()
                                .toInt() in 0..59) || timeString.subSequence(0, 2).toString()
                                .toInt() == 22 && timeString.subSequence(3, 5).toString()
                                .toInt() == 0
                        ) {
                            shift = "Night(19:00:22:00)"
                        }


                    }

                }
                val sdf = SimpleDateFormat("yyyyMMddHHmmss")
                val currentDateandTime: String = sdf.format(Date())

                var BookedBy = "None"
                var BookerNumber = "None"
                var bookerJobId = "none"
                var confirmation = "No"
                val provideJobItem =
                    ProvideWorkItem(
                        "No",
                        locationString,
                        jobName,
                        jobpay,
                        jobProvideName,
                        genderString,
                        jobAge,
                        timeString,
                        sharedPreferences.getString("MobileNo", "Mobile"),
                        slot,
                        shift,
                        currentDateandTime,
                        BookedBy,
                        BookerNumber,
                        bookerJobId,
                        confirmation

                    )

                var ref = firebase.getReference("UserPostedJob")
                var joblistref = firebase.getReference("JobList")
                if (timeString != " ") {
                    sharedPreferences.getString("MobileNo", "Mobile")?.let { it1 ->
                        ref.child(it1).child(currentDateandTime).setValue(provideJobItem)
                            .addOnCompleteListener {
                                if (isAdded) {
                                    Toast.makeText(
                                        activity as Context,
                                        "Success",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                    }
                    currentDateandTime.let { it1 ->
                        joblistref.child(it1).setValue(provideJobItem).addOnCompleteListener {
                            if (isAdded) {
                                Toast.makeText(activity as Context, "Success", Toast.LENGTH_SHORT)
                                    .show()
                            }


                        }
                    }
                    layoutPostJob.visibility = View.GONE
                    postAJob.text = "Post a Job"
                    tilJobName.editText?.text?.clear()
                    tilJobPay.editText?.text?.clear()
                    tilTimings.editText?.text?.clear()
                    tiljobLocation.editText?.text?.clear()
                    tilAge.editText?.text?.clear()
                    tilGender.editText?.text?.clear()

                } else {
                    if (isAdded) {
                        Toast.makeText(
                            activity as Context,
                            "Please choose time between 8 Am and 10 Pm",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

            } else {
                if (isAdded) {
                    Toast.makeText(
                        activity as Context,
                        "Please fill all details",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            }
        }

















        fetchPrevious()


        mActivity.refreshButton.setOnClickListener {
            fetchPrevious()
        }













        return view

    }

    private fun fetchPrevious() {
        provideArray.clear()
        btncheckPrev.setOnClickListener {
            if (recyclerCheckPrev.visibility == View.GONE) {
                if (provideArray.size == 0) {
                    if (isAdded) {
                        Toast.makeText(
                            activity as Context,
                            "You have not posted any job",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                recyclerCheckPrev.visibility = View.VISIBLE
                btncheckPrev.setText("Cancel")
            } else if (recyclerCheckPrev.visibility == View.VISIBLE) {
                recyclerCheckPrev.visibility = View.GONE
                btncheckPrev.setText("Check Previous Job Status")
            }

        }

        provideArray.clear()
        var userMobile = sharedPreferences.getString("MobileNo", "MobileNo")
        var databaseRef = FirebaseDatabase.getInstance().getReference("UserPostedJob/$userMobile")
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        if (isAdded) {
                            Toast.makeText(activity as Context, "No data", Toast.LENGTH_SHORT)
                                .show()
                        }

                    } else {
                        for (snap: DataSnapshot in snapshot.children) {
                            status = snap.child("book").getValue().toString()
                            pay = snap.child("jobPay").getValue().toString()
                            time = snap.child("jobTimings").getValue().toString()
                            location = snap.child("jobLocation").getValue().toString()
                            jobId = snap.child("id").getValue().toString()
                            bookerJobId = snap.child("bookerJobId").getValue().toString()

                            bookerNumer = snap.child("bookerNumber").getValue().toString()
                            bookedBy = snap.child("bookedBy").getValue().toString()
                            jobName = snap.child("jobName").getValue().toString()
                            confirmed = snap.child("confirmed").getValue().toString()


                            var model =
                                PostedWorkItem(
                                    status,
                                    pay,
                                    time,
                                    location,
                                    bookedBy,
                                    jobId,
                                    bookerJobId,
                                    bookerNumer,
                                    jobName,
                                    confirmed
                                )
                            provideArray.add(model)

                        }
                        print("my model array is $provideArray")

                        if (isAdded) {
                            provideJobAdaptor =
                                ProvideJobAdaptor(
                                    activity as Context,
                                    provideArray
                                )
                            recyclerCheckPrev.adapter = provideJobAdaptor
                            recyclerCheckPrev.layoutManager = layoutManager
                        }
                    }
                }

            })
    }


}
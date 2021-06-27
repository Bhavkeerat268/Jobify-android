package com.training.jobifi.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.database.*
import com.training.jobifi.modelClass.RecommendedJobs
import com.training.jobifi.adaptor.RecyclerViewRecAdaptor
import com.training.jobifi.activity.DashboardActivity
import com.training.jobify.R
import org.json.JSONArray
import org.json.JSONObject


class RecommendedFragment :
    Fragment() {                /*Fragment to show recommended jobs according to user preferences*/

    lateinit var mActivity: DashboardActivity
    lateinit var sharedPreferences: SharedPreferences
    lateinit var recyclerViewRec: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var imgNodata: LinearLayout
    lateinit var recyclerViewHomeAdaptor: RecyclerViewRecAdaptor
    lateinit var searchBar: EditText
    lateinit var shimmerLayout: ShimmerFrameLayout
    var gender: String? = null
    lateinit var city: String
    lateinit var job1: String
    lateinit var job2: String
    lateinit var job3: String
    lateinit var job4: String
    lateinit var job5: String
    lateinit var shift: String
    lateinit var age: String
    lateinit var combine: String
    var recommendedJobList = arrayListOf<RecommendedJobs>()

    //    lateinit var refreshListener: SwipeRefreshLayout
    var state = "not_refreshed"


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
        val view = inflater.inflate(R.layout.fragment_recommended, container, false)
        imgNodata = view.findViewById(R.id.noData)

        shimmerLayout = view.findViewById(R.id.shimmer_layout)
        recyclerViewRec = view.findViewById(R.id.recyclerViewRec)

        searchBar = view.findViewById(R.id.search)
        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file),
            Context.MODE_PRIVATE
        )

        if (isAdded) {
            layoutManager = LinearLayoutManager(activity as Context)
        }
        if (state == "refreshed") {
            displayData(recommendedJobList)

        }

        if (state == "not_refreshed") {
            shimmerLayout.startShimmer()
            userToKeyword()
        }


        mActivity.refreshButton.setOnClickListener {
            imgNodata.visibility = View.GONE
            recyclerViewRec.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE
            shimmerLayout.startShimmer()
            userToKeyword()
        }


        searchBar.addTextChangedListener(object : TextWatcher {      //searchbar listener
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })





        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun closclicked(): Boolean {
        searchBar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.getAction() === MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= searchBar.getRight() - searchBar.getCompoundDrawables()
                            .get(DRAWABLE_RIGHT).getBounds().width()
                    ) {
                        filter(searchBar.text.clear().toString())
                        return true
                    }
                }
                return false
            }

        })
        return false

    }

    /*In this we are making a collection of user characteristics and jobs preferences as a string to send to api to fetch suitable jobs from available job list */
    private fun userToKeyword() {


        val mobile = sharedPreferences.getString("MobileNo", "91XXXXXXXXXX")
        var slot: String = "not given"

        val checkuser: Query? =
            FirebaseDatabase.getInstance().getReference("Users")?.orderByChild("mobileNo")
                ?.equalTo(mobile)
        if (checkuser != null) {
            checkuser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(activity as Context, error.message, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    gender = mobile?.let {
                        snapshot.child(it).child("gender").getValue(String::class.java)
                    }
                    city = mobile?.let {
                        snapshot.child(it).child("city").getValue(String::class.java)
                    }.toString()
                    job1 = mobile?.let {
                        snapshot.child(it).child("job1").getValue(String::class.java)
                    }.toString()
                    job2 = mobile?.let {
                        snapshot.child(it).child("job2").getValue(String::class.java)
                    }.toString()
                    job3 = mobile?.let {
                        snapshot.child(it).child("job3").getValue(String::class.java)
                    }.toString()
                    job4 = mobile?.let {
                        snapshot.child(it).child("job4").getValue(String::class.java)
                    }.toString()
                    job5 = mobile?.let {
                        snapshot.child(it).child("job5").getValue(String::class.java)
                    }.toString()
                    age = mobile?.let {
                        snapshot.child(it).child("age").getValue(String::class.java)
                    }!!
                    shift = mobile.let {
                        snapshot.child(it).child("shift").getValue(String::class.java)
                    }.toString()
                    var uage = age.toInt()
                    when {
                        uage in 20..30 -> slot = "slotA"
                        uage in 31..40 -> slot = "slotB"
                        uage in 41..50 -> slot = "slotC"
                    }
                    combine = "$city $job1 $job2 $job3 $job4 $job5 $shift $slot $gender"
                    print("Combine list is $combine")
                    if (isAdded) {

                        getRecommendedList(combine)         /*Sending userkeyword to fetch job recommendations*/
                    }


                }

            })
        }


    }


    private fun filter(text: String) {                 //Function to implement search button functionality
        val filteredList = arrayListOf<RecommendedJobs>()

        if (closclicked() == false) {
            for (i in 0 until recommendedJobList.size) {
                val filter = recommendedJobList[i]
                if (filter.job.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(filter)
                }
            }
        }
        if (filteredList.isNotEmpty()) {
            recyclerViewHomeAdaptor =
                RecyclerViewRecAdaptor(
                    activity as Context,
                    recommendedJobList
                )
            recyclerViewRec.adapter = recyclerViewHomeAdaptor
            recyclerViewRec.layoutManager = layoutManager
            recyclerViewHomeAdaptor.filterList(filteredList)
        }

    }


    fun displayData(list: ArrayList<RecommendedJobs>) {              /*Function to display data*/
        if (list.size == 0) {
            imgNodata.visibility = View.VISIBLE
            shimmerLayout.visibility = View.GONE
            shimmerLayout.stopShimmer()
        } else {

        }
        recyclerViewRec.visibility = View.VISIBLE
        shimmerLayout.visibility = View.GONE
        shimmerLayout.stopShimmer()
        recyclerViewHomeAdaptor =
            RecyclerViewRecAdaptor(
                activity as Context,
                list
            )
        recyclerViewRec.layoutManager = layoutManager
        recyclerViewRec.adapter = recyclerViewHomeAdaptor


    }


    private fun getRecommendedList(keywords: String) {      /*Fetch recommendation list from api*/
        state = "refreshed"
        recommendedJobList.clear()
        if (isAdded) {

            val queue = Volley.newRequestQueue(activity as Context)

            val url = "https://jobify11.herokuapp.com/rec/"
            val jsonparams = JSONObject()
            jsonparams.put("ckeyword", keywords)
            val jsonObjectRequest = @RequiresApi(Build.VERSION_CODES.KITKAT)
            object : JsonObjectRequest(Method.POST, url, jsonparams,
                Response.Listener {

                    print("my json response is $it")
                    if (it != null) {

                        recyclerViewRec.visibility = View.VISIBLE
                        shimmerLayout.stopShimmer()
                        shimmerLayout.visibility = View.GONE
                    }

                    var jsonstring: String = it.getString("recommend")
                    if (jsonstring == "No data") {
                        shimmerLayout.stopShimmer()
                        shimmerLayout.visibility = View.GONE
                        imgNodata.visibility = View.VISIBLE
                        if (isAdded) {
                            Toast.makeText(
                                activity as Context,
                                "No data till now",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        print("My firebase is $jsonstring")
                        val reader = JSONArray(jsonstring)
                        if (reader.length() == 0) {
                            if (isAdded) {
                                Toast.makeText(activity as Context, "Thats all", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        for (i in 0 until reader.length()) {
                            var recJobList = reader.getJSONObject(i)
                            val recJob =
                                RecommendedJobs(
                                    recJobList.getString("jobname").toString(),
                                    recJobList.getString("jobprovidename").toString(),
                                    recJobList.getString("joblocation").toString(),
                                    recJobList.getString("id").toString(),
                                    recJobList.getString("jobprovnumber").toString(),
                                    recJobList.getString("book").toString()

                                )
                            val mobile = sharedPreferences.getString("MobileNo", "91XXXXXXXXXX")
                            if (recJobList.getString("jobprovnumber")
                                    .toString() != mobile && recJobList.getString("book")
                                    .toString() == "No"
                            ) {
                                recommendedJobList.add(recJob)
                            }


                        }
                    }

                    println("rec list is $recommendedJobList")
                    if (isAdded) {
                        if (recommendedJobList.size == 0) {
                            imgNodata.visibility = View.VISIBLE
                            Toast.makeText(activity as Context, "Thats all", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    if (isAdded) {


                        displayData(recommendedJobList)

                    }


                    closclicked()
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
            queue.add(jsonObjectRequest)


        }


    }

}

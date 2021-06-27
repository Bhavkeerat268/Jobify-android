package com.training.jobifi.fragment

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.facebook.shimmer.ShimmerFrameLayout
import com.training.jobifi.adaptor.HomeAdaptor
import com.training.jobifi.modelClass.RecommendedJobs
import com.training.jobifi.activity.DashboardActivity
import com.training.jobify.R


class HomeFragment : Fragment() {
    var userModalArrayList = arrayListOf<RecommendedJobs>()
    lateinit var mActivity: DashboardActivity
    lateinit var userRVAdapter: HomeAdaptor
    lateinit var userRV: RecyclerView
    lateinit var loadingPB: ProgressBar            /*Variables Declaration*/
    lateinit var nestedSV: NestedScrollView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var shimmerLayout: ShimmerFrameLayout
    lateinit var sharedPreferences: SharedPreferences
    lateinit var imgNOdata: LinearLayout
    var state = "non_refreshed"

    var page = 0
    var limit: Int = 50


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DashboardActivity) {
            mActivity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //


        val view = inflater.inflate(R.layout.fragment_home, container, false)
        imgNOdata = view.findViewById(R.id.imgNodata)
        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file),
            Context.MODE_PRIVATE
        )

        ActivityCompat.requestPermissions(
            mActivity,
            arrayOf(
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.READ_SMS
            ),
            PackageManager.PERMISSION_GRANTED
        )
        /*Variables Intialization*/
        userRV = view.findViewById(R.id.recyclerHome);
        loadingPB = view.findViewById(R.id.progressRecs);
        nestedSV = view.findViewById(R.id.idNestedSV);
        shimmerLayout = view.findViewById(R.id.shimmer_layout)

        if (isAdded) {
            layoutManager = LinearLayoutManager(activity as Context)
        }
        if (state == "refreshed")                              /*If state=refreshed then we will not fetch data again unless refresh button is pressed*/ {
            shimmerLayout.visibility = View.GONE
            shimmerLayout.stopShimmer()
            userRV.visibility = View.VISIBLE
            displayData(userModalArrayList)

        }

        if (state == "non_refreshed")             /*If state=non_refreshed then we will fetch data from api*/ {
            shimmerLayout.visibility = View.VISIBLE
            shimmerLayout.startShimmer()
            getDataFromApi(page, limit)
        }

        mActivity.refreshButton.setOnClickListener { /*Refresh buttton to fetch data again from api*/
            imgNOdata.visibility = View.GONE
            nestedSV.smoothScrollTo(0, 0, 500)
            userRV.visibility = View.GONE
            shimmerLayout.visibility = View.VISIBLE
            shimmerLayout.startShimmer()
            userModalArrayList.clear()
            getDataFromApi(page = 0, limit = 50)

        }


        /*Scroll view listener to fetch data when scroll view end is reached */
        nestedSV.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v != null) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    loadingPB.setVisibility(View.VISIBLE);
                    getDataFromApi(page, limit);
                }
            }
        }

        return view

    }


    /*Function to set adaptor to dsiplay data*/
    private fun displayData(userModalArrayList: ArrayList<RecommendedJobs>) {
        if (userModalArrayList.size == 0) {
            imgNOdata.visibility = View.VISIBLE
        } else {
            userRVAdapter =
                HomeAdaptor(
                    activity as Context,
                    userModalArrayList
                )
            userRV.layoutManager = layoutManager
            userRV.adapter = userRVAdapter

        }


    }


    /*Function to get data from api*/
    private fun getDataFromApi(page: Int, limit: Int) {
        state = "refreshed"


        if (page > limit) {
            // checking if the page number is greater than limit.
            // displaying toast message in this case when page>limit.
            if (isAdded) {
                Toast.makeText(activity as Context, "That's all the data..", Toast.LENGTH_SHORT)
                    .show();
            }


            // hiding our progress bar.
            loadingPB.setVisibility(View.GONE);
            return;

        }
        val queue = Volley.newRequestQueue(activity as Context)

        /*Preparing request queue*/
        val url =
            "https://jobify11.herokuapp.com/users?page=$page&size=$limit"  /*Api to fetch job lists with paging applied*/
        val jsonObjectRequest = @RequiresApi(Build.VERSION_CODES.KITKAT)
        object : JsonObjectRequest(Method.GET, url, null,
            Response.Listener {
                if (it != null) {
                    loadingPB.visibility = View.GONE
                }

                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
                userRV.visibility = View.VISIBLE

                val JsonArray = it.getJSONArray("items")
                if (JsonArray.length() == 0) {
                    if (isAdded) {
                        Toast.makeText(activity as Context, "Thats all", Toast.LENGTH_SHORT).show()

                    }

                    loadingPB.visibility = View.GONE
                }

                for (i in 0 until JsonArray.length()) {
                    val reader = JsonArray.getJSONObject(i)
                    if (reader.getString("JobName").toString() == "None") {
                        imgNOdata.visibility = View.VISIBLE
                    } else {
                        val model =
                            RecommendedJobs(
                                reader.getString("JobName")
                                    .toString(),       /*Setting json response*/
                                reader.getString("JobProvideName").toString(),
                                reader.getString("JobLocation").toString(),
                                reader.getString("Id").toString(),
                                reader.getString("JobProvNumber").toString(),
                                reader.getString("Book").toString()
                            )
                        var mobile = sharedPreferences.getString("MobileNo", "Mobile")


                        if (reader.getString("JobProvNumber")
                                .toString() != mobile && reader.getString("Book")
                                .toString() != "Yes"
                        ) {
                            userModalArrayList.add(model)
                        }






                        if (isAdded) {

                            if (userModalArrayList.size == 0) {

                                imgNOdata.visibility = View.VISIBLE
                                this.page++;
                                getDataFromApi(this.page, limit);
                            } else {
                                imgNOdata.visibility = View.GONE
                            }

                            userRVAdapter =
                                HomeAdaptor(
                                    activity as Context,
                                    userModalArrayList
                                )

                            userRV.layoutManager = LinearLayoutManager(activity as Context)
                            userRV.adapter = userRVAdapter


                        }


                    }


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

        queue.add(jsonObjectRequest)    /*Sending Api request*/


    }
}


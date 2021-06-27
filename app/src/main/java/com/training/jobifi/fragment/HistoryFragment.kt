package com.training.jobifi.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.training.jobifi.adaptor.HistoryAdaptor
import com.training.jobifi.modelClass.UserHistoryModel
import com.training.jobifi.activity.DashboardActivity
import com.training.jobify.R


class HistoryFragment : Fragment() {                 /*Fragment to store user History*/


    lateinit var mActivity: DashboardActivity

    lateinit var sharedPreferences: SharedPreferences
    lateinit var recHistory: RecyclerView                         /*Variables Declaration*/
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var historyAdaptor: HistoryAdaptor
    lateinit var progressBar: ProgressBar
    lateinit var noData: LinearLayout
    var list = arrayListOf<String>()

    var hisList = arrayListOf<UserHistoryModel>()

    var bookId: String? = null
    var jobName: String? = null
    var jobUser: String? = null
    var jobTime: String? = null
    var jobPay: String? = null
    var jobId: String? = null
    var status: String = ""


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
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recHistory = view.findViewById(R.id.recHis)
        layoutManager = LinearLayoutManager(activity as Context)
        progressBar = view.findViewById(R.id.progressBar)                               /*Variables Initialization*/
        noData = view.findViewById(R.id.noData)




        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file),
            Context.MODE_PRIVATE
        )
        val mobilenumber: String? = sharedPreferences.getString("MobileNo", "MobileNo")

        if (mobilenumber != null) {
            fetchHistory(mobilenumber)
        }

        mActivity.refreshButton.setOnClickListener {
            if (mobilenumber != null) {                                             /*Refresh button to fetch data again*/

                fetchHistory(mobilenumber)
            }
        }












        return view
    }

    private fun fetchHistory(mobile: String) {        /*FUnction to fetch user history from firebase realtime database*/

        hisList.clear()

        val ref =
            mobile?.let { FirebaseDatabase.getInstance().getReference("UserHistory").child(it) }







        if (ref != null) {
            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (!snapshot.exists()) {
                        if (isAdded) {
                            Toast.makeText(
                                activity as Context,
                                "No User History",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        progressBar.visibility = View.GONE
                        noData.visibility = View.VISIBLE
                    } else {
                        for (snap: DataSnapshot in snapshot.children) {
                            bookId = snap.child("orderId").getValue().toString()
                            jobName = snap.child("hisJobName").getValue().toString()
                            jobUser = snap.child("hisJobUser").getValue().toString()
                            jobTime = snap.child("hisTime").getValue().toString()
                            jobPay = snap.child("hisPay").getValue().toString()
                            jobId = snap.child("jobId").getValue().toString()
                            status = snap.child("status").getValue().toString()
                            val model =
                                UserHistoryModel(
                                    bookId!!, jobName, jobUser, jobTime, jobPay,
                                    jobId!!, status
                                )
                            hisList.add(model)
                            list.add("$bookId $jobName $jobUser $jobTime $jobPay")
                            progressBar.visibility = View.GONE

                        }
                        print("my model array is $hisList")
                        print("my sss is $list")
                        if (isAdded) {
                            historyAdaptor =
                                HistoryAdaptor(
                                    activity as Context,
                                    hisList
                                )
                            recHistory.adapter = historyAdaptor
                            recHistory.layoutManager = layoutManager
                        }

                    }


                }


            })
        }

    }


}


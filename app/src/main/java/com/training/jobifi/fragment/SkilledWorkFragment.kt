package com.training.jobifi.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.training.jobifi.activity.WorkActivity
import com.training.jobify.R


class SkilledWorkFragment : Fragment(),
    View.OnClickListener    /*Fragment to show skilled jobs to get user job preferences*/ {
    lateinit var mActivity: WorkActivity
    lateinit var btntuitions: TextView
    lateinit var btnplumbing: TextView
    lateinit var btnwelding: TextView
    lateinit var btnelectrician: TextView
    lateinit var btncarpentering: TextView
    lateinit var btnpainter: TextView
    var addedList = arrayListOf<String>()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is WorkActivity) {
            mActivity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_skilled_work, container, false)

        btntuitions = view.findViewById(R.id.tuitions)
        btnplumbing = view.findViewById(R.id.plumbing)
        btnwelding = view.findViewById(R.id.welding)
        btnelectrician = view.findViewById(R.id.electrician)
        btncarpentering = view.findViewById(R.id.carpentering)
        btnpainter = view.findViewById(R.id.painter)



        btntuitions.setOnClickListener(this)
        btnplumbing.setOnClickListener(this)
        btnwelding.setOnClickListener(this)
        btnelectrician.setOnClickListener(this)
        btncarpentering.setOnClickListener(this)
        btnpainter.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        if (v != null) {

            when (v.id) {

                R.id.tuitions -> {
                    flowbtnClick(btntuitions)
                    checkFinish()
                }
                R.id.plumbing -> {
                    flowbtnClick(btnplumbing)
                    checkFinish()
                }
                R.id.welding -> {
                    flowbtnClick(btnwelding)
                    checkFinish()
                }
                R.id.electrician -> {
                    flowbtnClick(btnelectrician)
                    checkFinish()
                }
                R.id.carpentering -> {
                    flowbtnClick(btncarpentering)
                    checkFinish()
                }
                R.id.painter -> {
                    flowbtnClick(btnpainter)
                    checkFinish()
                }

            }
        }
    }

    private fun checkFinish() {
        if (((mActivity.joblist1.count()) + 1) == 6) {
            mActivity.btnFinish.visibility = View.VISIBLE
        } else if (((mActivity.joblist1.count()) + 1) < 6) {
            mActivity.btnFinish.visibility = View.GONE
        }
    }

    private fun flowbtnClick(btn: TextView) {

        if (((mActivity.joblist1.count()) + 1) < 6) {
            if (!mActivity.joblist1.contains(btn.text.toString())) {
                addButton(btn)

            } else if (mActivity.joblist1.contains(btn.text.toString())) {
                removebtn(btn)

            }

        } else if (((mActivity.joblist1.count()) + 1) == 6) {
            if (mActivity.joblist1.contains(btn.text.toString())) {
                removebtn(btn)
            }

        }


    }

    private fun addButton(btn: TextView) {


        mActivity.joblist1.add(btn.text.toString())

        btn.setBackgroundResource(R.drawable.selected_flow_btn)
        btn.setTextColor(resources.getColor(R.color.white))


    }

    private fun removebtn(btn: TextView) {
        mActivity.joblist1.remove(btn.text.toString())

        btn.setBackgroundResource(R.drawable.flow_btn)
        btn.setTextColor(resources.getColor(R.color.appBlue))

    }

}
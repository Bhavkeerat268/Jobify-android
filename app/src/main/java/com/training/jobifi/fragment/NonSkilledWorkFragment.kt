package com.training.jobifi.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nex3z.flowlayout.FlowLayout
import com.training.jobifi.activity.WorkActivity
import com.training.jobify.R


class NonSkilledWorkFragment : Fragment(), View.OnClickListener {
    /*Fragment to show all non skilled job preference choice*/
    lateinit var flowLayout: FlowLayout
    lateinit var btncarWashing: TextView
    lateinit var btnDelivery: TextView
    lateinit var btnPacking: TextView
    lateinit var btnSweeping: TextView      /*Variables Declaration*/
    lateinit var btnSalesman: TextView
    lateinit var btnPromotions: TextView
    lateinit var btnHelper: TextView
    lateinit var btnProductsSampling: TextView
    lateinit var btnBrochureDist: TextView
    lateinit var btnProdBrand: TextView
    lateinit var btnLabour: TextView
    lateinit var mActivity: WorkActivity


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
        val view = inflater.inflate(R.layout.fragment_non_skilled_work, container, false)
        flowLayout = view.findViewById(R.id.skilledFlow)
        btncarWashing = flowLayout.findViewById(R.id.carWash)
        btnDelivery = flowLayout.findViewById(R.id.delivery)
        btnPacking = flowLayout.findViewById(R.id.packing)
        btnSweeping = flowLayout.findViewById(R.id.sweeping)
        btnSalesman = flowLayout.findViewById(R.id.salesman)
        btnPromotions = flowLayout.findViewById(R.id.promotions)
        btnHelper = flowLayout.findViewById(R.id.helper)
        btnProductsSampling = flowLayout.findViewById(R.id.products_sampling)
        btnBrochureDist = flowLayout.findViewById(R.id.brochure_distribution)
        btnProdBrand = flowLayout.findViewById(R.id.products_branding)
        btnLabour = flowLayout.findViewById(R.id.labour)

        if (mActivity.joblist1.count() == 5) {
            mActivity.btnFinish.visibility = View.VISIBLE
        }




        btncarWashing.setOnClickListener(this)
        btnDelivery.setOnClickListener(this)
        btnPacking.setOnClickListener(this)
        btnSweeping.setOnClickListener(this)
        btnSalesman.setOnClickListener(this)
        btnPromotions.setOnClickListener(this)
        btnHelper.setOnClickListener(this)
        btnProductsSampling.setOnClickListener(this)
        btnBrochureDist.setOnClickListener(this)
        btnProdBrand.setOnClickListener(this)
        btnLabour.setOnClickListener(this)




        return view
    }

    override fun onClick(v: View?) {    /*CLicking button and adding to job preference list*/
        if (v != null) {

            when (v.id) {

                R.id.carWash -> {
                    flowbtnClick(btncarWashing)
                    checkFinish()
                }
                R.id.delivery -> {
                    flowbtnClick(btnDelivery)
                    checkFinish()
                }
                R.id.packing -> {
                    flowbtnClick(btnPacking)
                    checkFinish()
                }
                R.id.sweeping -> {
                    flowbtnClick(btnSweeping)
                    checkFinish()
                }
                R.id.salesman -> {
                    flowbtnClick(btnSalesman)
                    checkFinish()
                }
                R.id.promotions -> {
                    flowbtnClick(btnPromotions)
                    checkFinish()
                }
                R.id.helper -> {
                    flowbtnClick(btnHelper)
                    checkFinish()
                }
                R.id.products_sampling -> {
                    flowbtnClick(btnProductsSampling)
                    checkFinish()
                }
                R.id.products_branding -> {
                    flowbtnClick(btnProdBrand)
                    checkFinish()
                }
                R.id.brochure_distribution -> {
                    flowbtnClick(btnBrochureDist)
                    checkFinish()
                }
                R.id.labour -> {
                    flowbtnClick(btnLabour)
                    checkFinish()
                }

            }
        }
    }

    private fun checkFinish() {                     /*Function to check atleast 5 jobs are there in the job preference list*/
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

    private fun addButton(btn: TextView) {         /*Button click functionality handling*/


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
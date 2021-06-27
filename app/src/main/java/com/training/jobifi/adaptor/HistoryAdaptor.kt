package com.training.jobifi.adaptor

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.training.jobifi.modelClass.UserHistoryModel
import com.training.jobify.R

class HistoryAdaptor(val context: Context, var hisArray: ArrayList<UserHistoryModel>) :
    RecyclerView.Adapter<HistoryAdaptor.HisViewHolder>() {                                                   /*Adaptor for user history*/

    class HisViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var jobname: TextView = view.findViewById(R.id.jobName)
        var jobPay: TextView =
            view.findViewById(R.id.jobpay)           /*Declaring views for our single item in adaptor*/
        var imgJob: ImageView = view.findViewById(R.id.imgg)
        var jobtime: TextView = view.findViewById(R.id.jobtime)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HisViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_item_history, parent, false)
        return HisViewHolder(view)                          /*Setting out single item layout*/
    }

    override fun getItemCount(): Int {
        return hisArray.size
    }


    override fun onBindViewHolder(
        holder: HisViewHolder,
        position: Int
    ) {                                                           /*Setting views values*/
        val data = hisArray[position]

        holder.jobname.text = "${data.hisJobName}"



        if (data.status == "Pending") {
            holder.jobname.setTextColor(Color.parseColor("#ee0000"))
        } else if (data.status == "Completed") {
            holder.jobname.setTextColor(Color.parseColor("#006400"))
        }

        holder.jobtime.text = data.hisTime
        holder.jobPay.text = "Job Pay : Rs ${data.hisPay}"
        when {
            data.hisJobName == "CarWashing" -> setupImage(holder, R.drawable.carwashimg)
            data.hisJobName == "Delivery" -> setupImage(holder, R.drawable.delivery)
            data.hisJobName == "Packing" -> setupImage(holder, R.drawable.packing)
            data.hisJobName == "Sweeping" -> setupImage(holder, R.drawable.sweeping)
            data.hisJobName == "Salesperson" -> setupImage(holder, R.drawable.salesman)
            data.hisJobName == "Promotions" -> setupImage(holder, R.drawable.promotion)
            data.hisJobName == "Helper" -> setupImage(holder, R.drawable.helper)
            data.hisJobName == "ProductsSampling" -> setupImage(holder, R.drawable.product)
            data.hisJobName == "BrochureDistribution" -> setupImage(holder, R.drawable.brouchers)
            data.hisJobName == "BrandingOfProducts" -> setupImage(holder, R.drawable.branding)
            data.hisJobName == "Electrician" -> setupImage(holder, R.drawable.electrician)
            data.hisJobName == "Labour" -> setupImage(holder, R.drawable.labour)
            data.hisJobName == "Tuitions" -> setupImage(holder, R.drawable.tutions)
            data.hisJobName == "Plumbing" -> setupImage(holder, R.drawable.plumbing)
            data.hisJobName == "Welding" -> setupImage(holder, R.drawable.welding)
            data.hisJobName == "Carpentering" -> setupImage(holder, R.drawable.carpenter)
            data.hisJobName == "Painter" -> setupImage(holder, R.drawable.painterr)

        }


    }

    private fun setupImage(holder: HisViewHolder, drawableid: Int) {
        Glide.with(context)
            .load(drawableid)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(holder.imgJob)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}
package com.training.jobifi.adaptor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.training.jobifi.modelClass.RecommendedJobs
import com.training.jobifi.activity.DetailsActivity
import com.training.jobify.R

class RecyclerViewRecAdaptor(val context: Context, var recjobArray:ArrayList<RecommendedJobs>):
    RecyclerView.Adapter<RecyclerViewRecAdaptor.RecViewHomeHolder>(){   /*Recommended jobs adaptor*/

    class RecViewHomeHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val txtJobname: TextView =view.findViewById(R.id.jobName)
        val txtJobUser: TextView =view.findViewById(R.id.jobUser)
        val txtJobcity: TextView =view.findViewById(R.id.jobCity)
       val imgjob:ImageView=view.findViewById(R.id.imgJob)
        val itemlayout:RelativeLayout=view.findViewById(R.id.itemlayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecViewHomeHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.single_item_layout,parent,false)
        return RecViewHomeHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return recjobArray.size
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onBindViewHolder(holder: RecViewHomeHolder, position: Int) {
        val recjobs=recjobArray[position]
        val shimmer: Shimmer =
            Shimmer.ColorHighlightBuilder().setBaseColor((Color.parseColor("#f3f3f3"))).setBaseAlpha(
            1F
        ).setHighlightColor(Color.parseColor("#e7e7e7")).setHighlightAlpha(1F).setDropoff(50F).build()
        val shimmerDrawable= ShimmerDrawable()
        shimmerDrawable.setShimmer(shimmer)
        holder.txtJobname.text=recjobs.job
        if (recjobs.JobUserName.count()>10)
        {
            holder.txtJobUser.text= recjobs.JobUserName.substring(0,9)
        }
          else
        {
            holder.txtJobUser.text= recjobs.JobUserName
        }

        when
        {
            recjobs.job=="CarWashing"->setupImage(holder,R.drawable.carwashimg)
            recjobs.job=="Delivery"->setupImage(holder,R.drawable.delivery)
            recjobs.job=="Packing"->setupImage(holder,R.drawable.packing)
            recjobs.job=="Sweeping"->setupImage(holder,R.drawable.sweeping)
            recjobs.job=="Salesperson"->setupImage(holder,R.drawable.salesman)
            recjobs.job=="Promotions"->setupImage(holder,R.drawable.promotion)
            recjobs.job=="Helper"->setupImage(holder,R.drawable.helper)
            recjobs.job=="ProductsSampling"->setupImage(holder,R.drawable.product)
            recjobs.job=="BrochureDistribution"->setupImage(holder,R.drawable.brouchers)
            recjobs.job=="BrandingOfProducts"->setupImage(holder,R.drawable.branding)
            recjobs.job=="Labour"->setupImage(holder,R.drawable.labour)
            recjobs.job=="Electrician"->setupImage(holder,R.drawable.electrician)
            recjobs.job=="Tuitions"->setupImage(holder,R.drawable.tutions)
            recjobs.job=="Plumbing"->setupImage(holder,R.drawable.plumbing)
            recjobs.job=="Welding"->setupImage(holder,R.drawable.welding)
            recjobs.job=="Carpentering"->setupImage(holder,R.drawable.carpenter)
            recjobs.job=="Painter"->setupImage(holder,R.drawable.painterr)

        }
        holder.txtJobcity.text=recjobs.location
        holder.itemlayout.setOnClickListener {

            val intent=Intent(context,
                DetailsActivity::class.java)
            intent.putExtra("jobName",recjobs.job)
            intent.putExtra("jobUser",recjobs.JobUserName)
            intent.putExtra("jobLocation",recjobs.location)
            intent.putExtra("id",recjobs.id)

            val options = ViewCompat.getTransitionName(holder.itemlayout)
                ?.let { ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, holder.itemlayout, it) }
            context.startActivity(intent, options?.toBundle())


        }




    }
    private fun setupImage(holder: RecViewHomeHolder, drawableid: Int)
    {
        Glide.with(context)
            .load(drawableid)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imgjob)
    }

    fun filterList(filteredList: ArrayList<RecommendedJobs>) {
        recjobArray=filteredList
        notifyDataSetChanged()

    }

}



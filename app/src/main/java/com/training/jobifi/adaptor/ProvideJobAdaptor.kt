package com.training.jobifi.adaptor

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.training.jobifi.modelClass.PostedWorkItem
import com.training.jobify.R

class ProvideJobAdaptor(val context: Context, var provideArray:ArrayList<PostedWorkItem>):  /*Adaptor for posted jobs*/
    RecyclerView.Adapter<ProvideJobAdaptor.ProvViewHolder>() {


    class ProvViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        var jobName:TextView=view.findViewById(R.id.JobName)
        var bookStatus: TextView =view.findViewById(R.id.status)
        var pay: TextView =view.findViewById(R.id.pay)
        var time: TextView =view.findViewById(R.id.time)
        var bookedBy: TextView=view.findViewById(R.id.bookedBy)
        var bookerPhone: TextView =view.findViewById(R.id.bookerPhone)
        var jobLocation:TextView=view.findViewById(R.id.location)
        var jobId: TextView =view.findViewById(R.id.jobId)
        var btnConfirm:Button=view.findViewById(R.id.btnConfirm)
        var imgDone:ImageView=view.findViewById(R.id.imgDone)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProvViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.single_posted_job_item,parent,false)
        return ProvViewHolder(view)
    }

    override fun getItemCount(): Int {
        return provideArray.size
    }

    override fun onBindViewHolder(holder: ProvViewHolder, position: Int) {


        var sharedPreferences=context.getSharedPreferences(context.getString(R.string.preference_file),Context.MODE_PRIVATE)
       var mobile=sharedPreferences.getString("MobileNo","Mobile")
        var data=provideArray[position]

        if(data.confirmed=="Yes" && data.status=="Yes")
        {
            holder.imgDone.visibility=View.VISIBLE
        }
        if(data.confirmed=="No" && data.status=="Yes")
        {
            holder.btnConfirm.visibility=View.VISIBLE
        }
        else if(data.status=="No")
        {
            holder.btnConfirm.visibility=View.GONE
        }
        holder.btnConfirm.setOnClickListener {

            var firebasref=FirebaseDatabase.getInstance().getReference("UserHistory/${data.bookerNumber}").child(data.bookerJobId).child("status").setValue("Completed").addOnCompleteListener {

                Toast.makeText(context as Activity, "Thanks", Toast.LENGTH_SHORT).show()
                holder.btnConfirm.visibility=View.GONE
                holder.imgDone.visibility=View.VISIBLE
                var ref=FirebaseDatabase.getInstance().getReference("UserPostedJob/$mobile").child(data.jobId).child("confirmed").setValue("Yes").addOnCanceledListener {
                    Toast.makeText(context as Activity, "Confirmed", Toast.LENGTH_SHORT).show()
                }

            }
        }
        holder.jobName.text=data.jobName
        holder.bookStatus.text="Booking status:${data.status}"

        holder.pay.text="Job Pay: Rs ${data.pay}"

        var time=" "
        if (data.time.subSequence(0,2).toString().toInt()<12)
        {
            time="${data.time} AM"
        }
        else if (data.time.subSequence(0,2).toString().toInt()==12)
        {
            time="${data.time} PM"
        }
        else if(data.time.subSequence(0,2).toString().toInt()>12)
        {
            time="${data.time.subSequence(0,2).toString().toInt()-12}:${data.time.subSequence(3,5)} PM"
        }
        holder.time.text="Job Time:$time"
        if (data.bookedBy=="null")
        {
            holder.bookedBy.text="Booked By:None"
        }
        else
        {
            holder.bookedBy.text="Booked By:${data.bookedBy}"
        }

        holder.bookerPhone.text="Booker Contacts:${data.bookerNumber}"
        holder.jobId.text="Job Id:${data.jobId}"
        holder.jobLocation.text="Job Location :${data.location}"

    }
}
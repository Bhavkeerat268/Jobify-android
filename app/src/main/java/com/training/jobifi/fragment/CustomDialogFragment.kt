package com.training.jobifi.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.training.jobify.R


class CustomDialogFragment :DialogFragment() {  /*Custom dialog */


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_custom_dialog, container, false)
        val btnGotIt=view.findViewById<Button>(R.id.btnGotIt)
        dialog?.getWindow()?.setBackgroundDrawableResource(R.drawable.dialog_rounded_background);
        btnGotIt.setOnClickListener {
            dialog?.dismiss()
        }
        return view
    }
}
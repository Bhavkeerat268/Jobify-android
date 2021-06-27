package com.training.jobifi.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.training.jobify.R

class BookSuccess : AppCompatActivity() {      /*Activity to show booking successful*/
    lateinit var btndash:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_success)
        btndash=findViewById(R.id.btnGoToDash)
        btndash.setOnClickListener {
            val intent=Intent(this, DashboardActivity::class.java)
            startActivity(intent)               /*Move to dashboard*/
            finish()
        }
    }
}
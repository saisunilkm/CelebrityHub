package com.example.celebrityhub

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val personId = intent.getIntExtra("id", -1)
        val personName = intent.getStringExtra("name")
        val personOverview = intent.getStringArrayExtra("overview")

        val idTextView: TextView = findViewById(R.id.detail_id)
        val nameTextView: TextView = findViewById(R.id.detail_name)
        val overView: TextView = findViewById(R.id.detail_overview)

        idTextView.text = "ID: "+ personId.toString()
        nameTextView.text = "Name: " + personName
        overView.text = "OverView: " + personOverview

    }
}


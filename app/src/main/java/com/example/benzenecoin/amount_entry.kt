package com.example.benzenecoin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class amount_entry : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amount_entry)

        val buttonClick = findViewById<ImageView>(R.id.back_button)
        buttonClick.setOnClickListener {
            val intent = Intent(this, transaction_activity::class.java)
            startActivity(intent)
        }
    }
}
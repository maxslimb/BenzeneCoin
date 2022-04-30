package com.example.benzenecoin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class buy_benzenecoin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_benzenecoin)

        val buttonClick = findViewById<Button>(R.id.buy_token_benze)
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val b = sharedPreferences.getString("balance","")
        val Balance_rec = findViewById<TextView>(R.id.Balance_bb)
        Balance_rec.text=b
        buttonClick.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
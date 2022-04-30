package com.example.benzenecoin

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class buy_benzenecoin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_benzenecoin)
        val amount_bb = findViewById<TextInputEditText>(R.id.amount_bb)
        val buttonClick = findViewById<Button>(R.id.buy_token_benze)
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val b = sharedPreferences.getString("balance","")
        val Balance_rec = findViewById<TextView>(R.id.Balance_bb)
        Balance_rec.text=b
        buttonClick.setOnClickListener {
           val amount = amount_bb.text.toString();
        }
    }
}
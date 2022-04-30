package com.example.benzenecoin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.textfield.TextInputEditText

class amount_entry : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amount_entry)
        val amount = findViewById<TextInputEditText>(R.id.amount_send)
        val back_button = findViewById<ImageView>(R.id.back_button_amount)
        val send_button_amount = findViewById<Button>(R.id.send_button_amount)

        send_button_amount.setOnClickListener {
            val intent = Intent(this, SenderActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("amount", amount.text.toString())
            startActivity(intent)
        }
        back_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
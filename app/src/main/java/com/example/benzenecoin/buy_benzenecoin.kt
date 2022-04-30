package com.example.benzenecoin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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


// Add the request to the RequestQueue.

        buttonClick.setOnClickListener {
           val amount = amount_bb.text.toString();
            val queue = Volley.newRequestQueue(this)
            val url = "http://13.234.204.127:5000/transfer"
            val stringRequest = StringRequest(
                Request.Method.POST, url,
                { response ->
                    // Display the first 500 characters of the response string.
                    Toast.makeText(applicationContext,"$response",Toast.LENGTH_SHORT).show()
                    val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                    var b = sharedPreferences.getString("balance","")
                    if (b != null) {
                        b = (b.toInt() + amount.toInt()).toString()
                    }
                    val editor: SharedPreferences.Editor= sharedPreferences.edit()
                    editor.putString("balance", b)
                    editor.commit()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                },
                { error -> Toast.makeText(applicationContext,"$error",Toast.LENGTH_SHORT).show()
                Log.d("kk","$error")})
            queue.add(stringRequest)
        }
    }
}
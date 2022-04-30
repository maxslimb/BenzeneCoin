package com.example.benzenecoin

import android.content.Intent
import android.content.SharedPreferences
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonClick = findViewById<Button>(R.id.send)
        val buttonClick1 = findViewById<Button>(R.id.receive)
        buttonClick.setOnClickListener {
            val intent = Intent(this, transaction_activity::class.java)
            startActivity(intent)
        }

        buttonClick1.setOnClickListener {
            val intent = Intent(this, receiving::class.java)
            startActivity(intent)
        }

        val wifi: WifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifi.isWifiEnabled = true
        wifi.isWifiEnabled = false

        val bt_send = findViewById<Button>(R.id.send)
        val bt_receive = findViewById<Button>(R.id.receive)
        val balance = findViewById<TextView>(R.id.Balance)
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor= sharedPreferences.edit()
        if(!sharedPreferences.getBoolean("updateb_once",false)) {
            editor.putBoolean("updateb_once", true)
            editor.putString("balance", "2000")
            editor.commit()
        }
        balance.text = sharedPreferences.getString("balance","0")
        CheckPermission()



        val web3 = Web3j.build(HttpService("https://rinkeby.infura.io/v3/03dfd526311a4ce3933474b193a25238"));
        try {
            val clientVersion = web3.web3ClientVersion().sendAsync().get()
            if (!clientVersion.hasError()) {
                Toast.makeText(this,"Connected",Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this,"Errorww",Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
        }

    }
}
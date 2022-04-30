package com.example.benzenecoin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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
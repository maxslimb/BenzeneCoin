package com.example.benzenecoin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

class SenderActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var amount: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
         sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val b = sharedPreferences.getString("balance","")
        val Balance_rec = findViewById<TextView>(R.id.Balance_trans)
        Balance_rec.text=b
        amount = intent.getStringExtra("amount").toString()

        val buttonClick = findViewById<ImageView>(R.id.back_button_transaction)
        buttonClick.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    override fun onStart() {
        super.onStart()
        startDiscovery()
    }

    private fun startDiscovery() {
        val SERVICE_ID = "com.example.benzenecoin"
        val discoveryOptions = DiscoveryOptions.Builder().setStrategy(Strategy.P2P_POINT_TO_POINT).build()
        Nearby.getConnectionsClient(applicationContext)
            .startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
            .addOnSuccessListener { unused: Void? ->
                Toast.makeText(applicationContext,"Connecting to peer",Toast.LENGTH_SHORT).show()
                Log.d("Sender","Discovery success")
            }
            .addOnFailureListener { e: Exception? ->
                Toast.makeText(applicationContext,"Error Connecting to teacher: ${e.toString()}",Toast.LENGTH_SHORT).show()
            }
    }
    private val endpointDiscoveryCallback: EndpointDiscoveryCallback =
        object : EndpointDiscoveryCallback() {
            override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
                // An endpoint was found. We request a connection to it.
                Nearby.getConnectionsClient(applicationContext)
                    .requestConnection("BenzeneCoin", endpointId, connectionLifecycleCallback) //change to faulty name- first param
                    .addOnSuccessListener(
                        OnSuccessListener { unused: Void? -> })
                    .addOnFailureListener(
                        OnFailureListener { e: java.lang.Exception? -> })
            }

            override fun onEndpointLost(endpointId: String) {
                // A previously discovered endpoint has gone away.
            }
        }
    private val payloadCallback: PayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            val data_recieved = payload.asBytes()?.let { String(it, Charsets.UTF_8) }
            Toast.makeText(applicationContext,"Data received $data_recieved",Toast.LENGTH_SHORT).show()
            if(data_recieved=="Success"){
                val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                var b = sharedPreferences.getString("balance","")
                if (b != null) {
                    b = (b.toInt() - amount.toInt()).toString()
                }
                val editor: SharedPreferences.Editor= sharedPreferences.edit()
                editor.putString("balance", b)
                editor.commit()
                Nearby.getConnectionsClient(applicationContext).stopDiscovery()
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {

        }
    }
    private val connectionLifecycleCallback: ConnectionLifecycleCallback =
        object : ConnectionLifecycleCallback() {
            override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
                // Automatically accept the connection on both sides.
                Log.d("Sender", "onConnectionInitiated: accepting connection")
                Nearby.getConnectionsClient(applicationContext).acceptConnection(endpointId,payloadCallback)
            }

            override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                when (result.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        // Once you have successfully connected to your friends' devices, you can leave
                        // discovery mode so you can stop discovering other devices

                        // if you were advertising, you can stop as well
                        Toast.makeText(applicationContext,"EndID of Receiver: $endpointId", Toast.LENGTH_SHORT).show()
                        val data = Payload.fromBytes(amount.toByteArray())
                        Nearby.getConnectionsClient(applicationContext).sendPayload(endpointId, data)
                        Nearby.getConnectionsClient(applicationContext).stopDiscovery()
                        //  friendEndpointId = endpointId

                    }
                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                    }
                    ConnectionsStatusCodes.STATUS_ERROR -> {
                    }
                    else -> {
                    }
                }
            }

            override fun onDisconnected(endpointId: String) {
                // We've been disconnected from this endpoint. No more data can be
                // sent or received.
            }
        }
    override fun onStop() {
        super.onStop()
        Nearby.getConnectionsClient(applicationContext).stopDiscovery()
        Log.d("Sender","Discovery stopped")
    }
}
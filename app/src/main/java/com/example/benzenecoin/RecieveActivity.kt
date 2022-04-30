package com.example.benzenecoin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

class RecieveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiving)

        val buttonClick = findViewById<ImageView>(R.id.back_button_receiving)
        buttonClick.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }
    override fun onStart() {
        super.onStart()
        startAdvertising()
    }
    override fun onStop() {
        super.onStop()
        Nearby.getConnectionsClient(applicationContext).stopAdvertising()
    }

    private fun startAdvertising() {
        val SERVICE_ID = "com.example.benzenecoin"
        val advertisingOptions = AdvertisingOptions.Builder().setStrategy(Strategy.P2P_POINT_TO_POINT).build()
        Nearby.getConnectionsClient(applicationContext)
            .startAdvertising(
                "BenzeneCoin", SERVICE_ID, connectionLifecycleCallback, advertisingOptions   //change first param to name of Teacher
            )
            .addOnSuccessListener(
                OnSuccessListener { unused: Void? ->

                    Toast.makeText(applicationContext,"Ready to Receive",Toast.LENGTH_SHORT).show()
                    Log.d("Receiver","Advertising success")

                })
            .addOnFailureListener(
                OnFailureListener { e: Exception? ->
                    Log.d("Receiver","Advertising fail: ${e.toString()}")
                    Toast.makeText(applicationContext,"Error Initialising: ${e.toString()}",Toast.LENGTH_SHORT).show()
                })
    }

    private val payloadCallback: PayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {

            val data_recieved = payload.asBytes()?.let { String(it, Charsets.UTF_8) }
            Log.d("Receiver","Data recieved: ${payload.asBytes()?.let { String(it, Charsets.UTF_8) }}")
            val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            var b = sharedPreferences.getString("balance","")
            if (b != null) {
                if (data_recieved != null) {
                    b = (b.toInt() + data_recieved.toInt()).toString()
                }
            }
            val editor: SharedPreferences.Editor= sharedPreferences.edit()
            editor.putString("balance", b)
            editor.commit()

            Toast.makeText(applicationContext,"Data recieved: ${payload.asBytes()?.let { String(it, Charsets.UTF_8) }}",Toast.LENGTH_SHORT).show()

            val data = Payload.fromBytes("Success".toByteArray())
            Nearby.getConnectionsClient(applicationContext).sendPayload(endpointId, data)
            Nearby.getConnectionsClient(applicationContext).stopAdvertising()
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {

        }
    }
    private val connectionLifecycleCallback: ConnectionLifecycleCallback =
        object : ConnectionLifecycleCallback() {
            override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
                Nearby.getConnectionsClient(applicationContext).acceptConnection(endpointId, payloadCallback)
            }

            override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                when (result.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        Toast.makeText(applicationContext,"EndID of Sender: $endpointId",Toast.LENGTH_SHORT).show()
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

            }
        }
}
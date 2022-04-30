package com.example.benzenecoin

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bt_send = findViewById<Button>(R.id.send_main)
        val bt_receive = findViewById<Button>(R.id.receive_main)
        bt_send.setOnClickListener {
            Onlocation()
        }
        val buttonClick2 = findViewById<Button>(R.id.buy_token_main)
        bt_receive.setOnClickListener {
            val intent = Intent(this, RecieveActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        buttonClick2.setOnClickListener {
            val intent = Intent(this, buy_benzenecoin::class.java)
            startActivity(intent)
        }

        val wifi: WifiManager =
            getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager


        val balance = findViewById<TextView>(R.id.Balance)
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        if (!sharedPreferences.getBoolean("updateb_once", false)) {  //test
            editor.putBoolean("updateb_once", true)
            editor.putString("balance", "2000")
            editor.commit()
        }
        balance.text = sharedPreferences.getString("balance", "0")
        CheckPermission()
    }
        private fun CheckPermission() {
            Dexter.withContext(applicationContext)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener, MultiplePermissionsListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {

                    }

                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {

                    }

                })
                .check();
        }

    private fun Onlocation(){

       val locationRequest = LocationRequest.create().apply {
           interval = 10000
           fastestInterval = 5000
           priority = LocationRequest.PRIORITY_LOW_POWER
       }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            val intent = Intent(this, amount_entry::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            Log.d("MainActivity","task called")
        }

        task.addOnFailureListener { exception ->

            if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    val REQUEST_CHECK_SETTINGS =1
                    exception.startResolutionForResult(this@MainActivity,
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }

    }
}
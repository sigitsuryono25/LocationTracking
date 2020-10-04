package com.surelabs.lauwaba.locationtracking

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*


class MyLocationServices : Service() {

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var mLocationCallback: LocationCallback
    private var mIsReceivingUpdates = false
    private val callback: UserLocationCallback = UserLocationCallback()

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getCurrentLocationUpdates(callback)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun getCurrentLocationUpdates(callback: UserLocationCallback) {
        if (mIsReceivingUpdates) {
            callback.onFailedRequest("Device is already receiving updates")
            return
        }

        // Set up the LocationCallback for the request
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                callback.onLocationResult(locationResult?.lastLocation)
            }
        }

        // Start the request
        val mLocationRequest = LocationRequest()
        mLocationRequest.apply {
            interval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val checkPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient?.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                null
            )
        }
        // Update the request state flag
        mIsReceivingUpdates = true
    }

    override fun onDestroy() {
        stopLocationUpdates()
        super.onDestroy()
    }

    private fun stopLocationUpdates() {
        mFusedLocationProviderClient?.removeLocationUpdates(mLocationCallback)
        mIsReceivingUpdates = false
        Log.i("Remove Location", "Location updates removed")
    }

    inner class UserLocationCallback {
        fun onLocationResult(location: Location?) {
            Toast.makeText(
                this@MyLocationServices,
                "${location?.latitude}, ${location?.longitude}",
                Toast.LENGTH_SHORT
            ).show()
        }

        fun onFailedRequest(message: String?) {
            Toast.makeText(this@MyLocationServices, message, Toast.LENGTH_SHORT).show()
        }


    }
}

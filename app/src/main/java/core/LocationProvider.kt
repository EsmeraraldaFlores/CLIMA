package com.example.clima.view.home
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class LocationProvider(private val activity: Activity) {
    private val fusedClient = LocationServices.getFusedLocationProviderClient(activity)

    fun checkPermissions(): Boolean =
        ActivityCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    fun getLastLocation(onLocationResult: (Location?) -> Unit) {
        if (!checkPermissions()) {
            onLocationResult(null)
            return
        }

        val request = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        try {
            fusedClient.getCurrentLocation(request, null)
                .addOnSuccessListener(onLocationResult)
                .addOnFailureListener {
                    it.printStackTrace()
                    onLocationResult(null)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            onLocationResult(null)
        }
    }
}

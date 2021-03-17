package com.example.mapapplication.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.mapapplication.R
import com.example.mapapplication.data.model.GeoSearch
import com.example.mapapplication.di.component.ActivityComponent
import com.example.mapapplication.ui.base.BaseActivity
import com.example.mapapplication.ui.details.DetailActivity
import com.example.mapapplication.utils.log.Logger
import com.example.mapapplication.utils.map.MapsFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapActivity : BaseActivity<MapViewModel>(), OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    companion object {
        private const val TAG = "LocationProvider"
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var currentLocation: Location? = null
    private lateinit var mMap: GoogleMap
    private val markerHashMap: HashMap<Marker, GeoSearch> = HashMap()
    private lateinit var currentLocationMarker: Marker

    override fun provideLayoutId(): Int = R.layout.activity_maps

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        addMapFragment()
    }

    override fun onStart() {
        super.onStart()
        if (!checkGPSEnabled()) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                //Location Permission already granted
                getLastLocation()
            } else {
                //Request Location Permission
                checkLocationPermission()
            }
        } else {
            getLastLocation()
        }
    }

    private fun addMapFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSIONS_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show()
                        getLastLocation()
                    }
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to use this app")
                .setPositiveButton("Location Settings") { _, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                }
                .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> }
        dialog.show()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                this.currentLocation = task.result

                val currentLocation = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                // Add current location marker
                currentLocationMarker = mMap.addMarker(MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_location)))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))

                viewModel.loadArticleLocations(generateGSCoordString(this.currentLocation))
            } else {
                Logger.w(TAG, "getLastLocation:exception")
                showMessage("No location detected. Make sure location is enabled on the device.")
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    )
            ) {
                AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK") { dialog, which ->
                            ActivityCompat.requestPermissions(
                                    this,
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    REQUEST_PERMISSIONS_REQUEST_CODE
                            )
                        }
                        .create()
                        .show()

            } else ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun checkGPSEnabled(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.uber_style
                )
        )
        mMap.setOnMarkerClickListener(this)
    }

    /**
     * Method to generate string param which need to fetch Article data
     */
    private fun generateGSCoordString(lastLocation: Location?): String {
        return "${lastLocation?.latitude}|${lastLocation?.longitude}"
    }

    override fun setupObservers() {
        super.setupObservers()
        // Show nearby markers
        viewModel.listGeoSearch.observe(this, Observer { it ->
            it.forEach {
                val location = LatLng(it.lat, it.lon)
                val marker = mMap.addMarker(MarkerOptions().position(location).title(it.title).icon(
                        BitmapDescriptorFactory.fromBitmap(MapsFactory.drawMarker(this, "W"))))
                markerHashMap[marker] = it
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13F))
            }
        })
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (markerHashMap[marker] != null) {
            val intent = Intent(this@MapActivity, DetailActivity::class.java)
            intent.putExtra("GeoSearch", markerHashMap[marker])
            intent.putExtra("CurrentLocation", currentLocation)
            startActivity(intent)
        }
        return true
    }
}
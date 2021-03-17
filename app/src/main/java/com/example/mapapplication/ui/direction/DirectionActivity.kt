package com.example.mapapplication.ui.direction


import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.example.mapapplication.R
import com.example.mapapplication.data.model.GeoSearch
import com.example.mapapplication.di.component.ActivityComponent
import com.example.mapapplication.ui.base.BaseActivity
import com.example.mapapplication.utils.log.Logger

import com.example.mapapplication.utils.map.MapsFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import kotlinx.android.synthetic.main.activity_direction.*
import java.io.IOException
import java.util.*

class DirectionActivity : BaseActivity<DirectionViewModel>(), OnMapReadyCallback {

    private lateinit var geoSearch: GeoSearch
    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: Location
    private var mRouteMarkerList = ArrayList<Marker>()
    private lateinit var mRoutePolyline: Polyline

    override fun provideLayoutId(): Int = R.layout.activity_direction

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.uber_style
                )
        )
        viewModel.fetchRoute(fetchAddress(currentLocation.latitude, currentLocation.longitude), fetchAddress(geoSearch.lat, geoSearch.lon))
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        geoSearch = intent.getParcelableExtra<GeoSearch>("GeoSearch") as GeoSearch
        currentLocation = intent.getParcelableExtra<Location>("CurrentLocation") as Location

        addMapFragment()
    }

    private fun addMapFragment() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map_direction) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.routes.observe(this, Observer {
            val startLatLng = LatLng(it.startLat!!, it.startLng!!)
            val startMarkerOptions: MarkerOptions = MarkerOptions().position(startLatLng).title(it.startName).icon(
                    BitmapDescriptorFactory.fromBitmap(MapsFactory.drawMarker(this, "S")))
            val endLatLng = LatLng(it.endLat!!, it.endLng!!)
            val endMarkerOptions: MarkerOptions = MarkerOptions().position(endLatLng).title(it.endName).icon(
                    BitmapDescriptorFactory.fromBitmap(MapsFactory.drawMarker(this, "E")))
            val startMarker = mMap.addMarker(startMarkerOptions)
            val endMarker = mMap.addMarker(endMarkerOptions)
            mRouteMarkerList.add(startMarker)
            mRouteMarkerList.add(endMarker)

            val polylineOptions = MapsFactory.drawRoute(this)
            val pointsList = PolyUtil.decode(it.overviewPolyline)
            for (point in pointsList) {
                polylineOptions.add(point)
            }

            mRoutePolyline = mMap.addPolyline(polylineOptions)

            mMap.animateCamera(MapsFactory.autoZoomLevel(mRouteMarkerList))
        })

        viewModel.routeInfoData.observe(this, Observer {
            route_info.text = it
        })
    }

    private fun fetchAddress(latitude: Double, longitude: Double): String {
        val result = StringBuilder()

        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> =
                    geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.size > 0) {
                val address: Address = addresses[0]
                result.append(address.postalCode).append("\n")
                result.append(address.locality).append("\n")
                result.append(address.adminArea).append("\n")
                result.append(address.countryName)
            }
        } catch (e: IOException) {
            Logger.e("tag", e.message.toString(), "")
        }
        return result.toString()
    }
}
package com.example.mapapplication.ui.details

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapapplication.R
import com.example.mapapplication.data.model.GeoSearch
import com.example.mapapplication.di.component.ActivityComponent
import com.example.mapapplication.ui.base.BaseActivity
import com.example.mapapplication.ui.direction.DirectionActivity
import com.example.mapapplication.utils.map.MapsFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.content_detail.*
import javax.inject.Inject


class DetailActivity : BaseActivity<DetailViewModel>(), OnMapReadyCallback {

    private lateinit var geoSearch: GeoSearch
    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: Location
    private var link: String? = null

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var imageAdapter: ImageAdapter

    override fun provideLayoutId(): Int = R.layout.activity_detail

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        geoSearch = intent.getParcelableExtra<GeoSearch>("GeoSearch") as GeoSearch
        currentLocation = intent.getParcelableExtra<Location>("CurrentLocation") as Location

        addMapFragment()

        image_list.layoutManager = linearLayoutManager
        image_list.adapter = imageAdapter

        text_get_there.setOnClickListener{
            val intent = Intent(this@DetailActivity, DirectionActivity::class.java)
            intent.putExtra("GeoSearch", geoSearch)
            intent.putExtra("CurrentLocation", currentLocation)
            startActivity(intent)
        }

        article_url.setOnClickListener{
            link?.let {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(browserIntent)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        mMap = googleMap!!
        // Add a marker
        val selectedLocation = LatLng(geoSearch.lat, geoSearch.lon)
        mMap.addMarker(MarkerOptions().position(selectedLocation).title(geoSearch.title).icon(
                BitmapDescriptorFactory.fromBitmap(MapsFactory.drawMarker(this, "W"))))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedLocation))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14F))
        mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.uber_style
            )
        )

        viewModel.fetchArticleDetail(geoSearch.pageId)
        viewModel.fetchPageUrl(geoSearch.pageId)
    }

    private fun addMapFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_detail) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun setupObservers() {
        super.setupObservers()

        viewModel.page.observe(this, Observer {
            article_title.text = it.title
            article_description.text = Html.fromHtml(it.extract, Html.FROM_HTML_MODE_COMPACT)
            viewModel.fetchImageUrl(it.images)
        })

        viewModel.imageList.observe(this, Observer {
            it?.run { imageAdapter.appendData(this) }
        })

        viewModel.pageUrl.observe(this, Observer {
            link = it
        })

    }
}
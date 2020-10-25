package com.example.parkingfinder

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.parkingfinder.api.RetrofitClient
import com.example.parkingfinder.models.GreyResponse
import com.example.parkingfinder.models.MapsViewModel
import com.example.parkingfinder.storage.SharedPrefManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.park_dialog.*
import kotlinx.android.synthetic.main.parking_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnPolygonClickListener {

    lateinit var viewModel: MapsViewModel
    private lateinit var map: GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1

    private val COLOR_WHITE_ARGB = -0x1
    private val COLOR_GREEN_ARGB = -0xc771c4
    private val COLOR_PURPLE_ARGB = -0x7e387c
    private val COLOR_BLACK_ARGB = 0x657db
    private val POLYGON_STROKE_WIDTH_PX = 8

    companion object {
        private const val CAMERA_PERMISSION_CODE = 2
        private const val CAMERA_REQUEST_CODE = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        viewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val latitude = 45.458515
        val longitude = -73.866868
        val zoomLevel = 20f

        val homeLatLng = LatLng(latitude, longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
        map.addMarker(MarkerOptions().position(homeLatLng))
        setMapLongClick(map)

        enableMyLocation()

        drawPolygon(map)

        map.setOnPolygonClickListener(this)
    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            // A Snippet is Additional text that's displayed below the title.
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)

            )
        }
    }

    private fun drawPolygon(googleMap: GoogleMap) {
        // Add polygons to indicate areas on the map.
        val polygon1 = googleMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(45.45881, -73.86714),
                    LatLng(45.45876, -73.86709),
                    LatLng(45.45875, -73.86711),
                    LatLng(45.45880, -73.86716)
                )
        )
        polygon1.tag = "alpha"
        stylePolygon(polygon1)

        val polygon2 = googleMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(45.45869, -73.86703),
                    LatLng(45.45862, -73.86697),
                    LatLng(45.45861, -73.86699),
                    LatLng(45.45868, -73.86705)
                )
        )
        polygon2.tag = "alpha"
        stylePolygon(polygon2)

        val polygon3 = googleMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(45.45855, -73.86689),
                    LatLng(45.45848, -73.86682),
                    LatLng(45.45847, -73.86684),
                    LatLng(45.45854, -73.86691)
                )
        )
        polygon3.tag = "grey"
        stylePolygon(polygon3)

        val polygon4 = googleMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(45.45839, -73.86674),
                    LatLng(45.45834, -73.86669),
                    LatLng(45.45833, -73.86671),
                    LatLng(45.45838, -73.86676)
                )
        )
        polygon4.tag = "beta"
        stylePolygon(polygon4)

        val polygon5 = googleMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(45.45817, -73.86676),
                    LatLng(45.45815, -73.86674),
                    LatLng(45.45810, -73.86684),
                    LatLng(45.45812, -73.86686)
                )
        )
        polygon5.tag = "grey"
        stylePolygon(polygon5)

        val polygon6 = googleMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(45.45889, -73.86739),
                    LatLng(45.45887, -73.86736),
                    LatLng(45.45884, -73.86744),
                    LatLng(45.45886, -73.86746)
                )
        )
        polygon6.tag = "grey"
        stylePolygon(polygon6)
    }

    /**
     * Styles the polygon, based on type.
     * @param polygon The polygon object that needs styling.
     */
    private fun stylePolygon(polygon: Polygon?) {
        // Get the data object stored with the polygon.
        val type = polygon?.tag?.toString() ?: ""
        var pattern: List<PatternItem>? = null
        var strokeColor = COLOR_BLACK_ARGB
        var fillColor = COLOR_WHITE_ARGB
        when (type) {
            "alpha" -> {
                strokeColor = COLOR_GREEN_ARGB
                fillColor = COLOR_PURPLE_ARGB
            }
            "beta" -> {
                strokeColor = Color.MAGENTA
                fillColor = Color.RED
            }
            "grey" -> {
                strokeColor = Color.BLACK
                fillColor = Color.GRAY
            }
        }
        polygon?.strokePattern = pattern
        polygon?.strokeWidth = POLYGON_STROKE_WIDTH_PX.toFloat()
        polygon?.strokeColor = strokeColor
        polygon?.fillColor = fillColor
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            REQUEST_LOCATION_PERMISSION ->
                if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                    enableMyLocation()
                }
            CAMERA_PERMISSION_CODE ->
                if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, CAMERA_REQUEST_CODE)
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                val thumbNail: Bitmap = data?.extras?.get("data") as Bitmap
                Toast.makeText(applicationContext, "picture taken", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // Change the map type based on the user's selection.
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        R.id.Reward -> {
            startActivity(Intent(this, RewardActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onPolygonClick(polygon: Polygon?) {
        when (polygon?.tag) {
            "alpha" -> parkDialog(polygon)
            "beta" -> leaveDialog(polygon)
            "grey" -> parkingDialog(polygon)
        }
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun parkingDialog(polygon: Polygon?) {
        val parkingDialog = Dialog(this)
        parkingDialog.setContentView(R.layout.parking_dialog)
        parkingDialog.parking_yes.setOnClickListener {
            viewModel.parkingRequest(true, 1,1,applicationContext)
            polygon?.tag = "beta"
            stylePolygon(polygon)
            parkingDialog.dismiss()
        }
        parkingDialog.parking_no.setOnClickListener {
            viewModel.parkingRequest(false, 1,1,applicationContext)
            requestCameraPermission()
            polygon?.tag = "alpha"
            stylePolygon(polygon)
            parkingDialog.dismiss()
        }
        parkingDialog.show()
    }

    private fun parkDialog(polygon: Polygon?) {
        val leavingDialog = Dialog(this)
        leavingDialog.setContentView(R.layout.park_dialog)
        leavingDialog.park_yes.setOnClickListener {
            viewModel.parkingRequest(true, 1,1,applicationContext)
            polygon?.tag = "beta"
            stylePolygon(polygon)
            leavingDialog.dismiss()
        }
        leavingDialog.park_no.setOnClickListener {
            leavingDialog.dismiss()
        }
        leavingDialog.show()
    }

    private fun leaveDialog(polygon: Polygon?) {
        val occupiedDialog = Dialog(this)
        occupiedDialog.setContentView(R.layout.leave_dialog)
        val yesBtn = occupiedDialog.findViewById(R.id.leaving_yes) as TextView
        val noBtn = occupiedDialog.findViewById(R.id.leaving_no) as TextView
        yesBtn.setOnClickListener {
            viewModel.parkingRequest(false, 1,1,applicationContext)
            polygon?.tag = "alpha"
            stylePolygon(polygon)
            occupiedDialog.dismiss()
        }
        noBtn.setOnClickListener {
            occupiedDialog.dismiss()
        }
        occupiedDialog.show()
    }
}

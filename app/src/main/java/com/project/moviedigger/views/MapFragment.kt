package com.project.moviedigger.views

import android.Manifest
import android.app.Activity
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.project.moviedigger.R
import com.project.moviedigger.interfaces.GoogleMapApiService
import com.project.moviedigger.models.MovieTheater
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// The code in this file is complete trash, do not touch it, ty :)
class MapFragment : OnMapReadyCallback,DialogFragment() {

    private lateinit var map: GoogleMap
    private lateinit var suppMap: SupportMapFragment
    private lateinit var apiService: GoogleMapApiService
    private lateinit var dataset: ArrayList<MovieTheater>
    private lateinit var myMarker: MarkerOptions
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var userPrefs: SharedPreferences
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var closeMapButton : Button

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dataset = ArrayList<MovieTheater>()
        return inflater.inflate(R.layout.fragment_map, container, false )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        suppMap = (fragmentManager?.findFragmentById(R.id.map) as SupportMapFragment)
        userPrefs = view.context.getSharedPreferences("userdetails", AppCompatActivity.MODE_PRIVATE)
        askPermission()
        locationRequest = LocationRequest.create()
        locationRequest.interval = 0;
        locationRequest.fastestInterval = 0;
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?: return
                for (location in locationResult.locations){
                    if (location != null) {
                        userPrefs.edit().putString("LOCATION_LAT", location.latitude.toString()).apply()
                        userPrefs.edit().putString("LOCATION_LON", location.longitude.toString()).apply()
                        getCurrentLocation()
                    }
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(view.context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(view.context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view?.context!!)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
        apiService = GoogleMapApiService.createService()
        closeMapButton = view.findViewById(R.id.close_map)
        closeMapButton.setOnClickListener{
            dismiss()
        }
    }

    private fun getPlaces() {
        apiService.getNearbyMovieTheaters(
                "${userPrefs.getString("LOCATION_LAT", "null")},${userPrefs.getString("LOCATION_LON", "null")}",
                "5000",
                "movie_theater",
                "true",
                getString(R.string.map_key))
                .enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Throw exception or something???
                        Log.d("filip", "no response")
                    }
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        val json = JSONObject(response.body()!!.string())
                        val results: JSONArray = json.getJSONArray("results")
                        for (i in 0 until results.length()) {
                            dataset.add(
                                    MovieTheater(
                                            results.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                            results.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng"),
                                            results.getJSONObject(i).getString("name")
                                    )
                            )
                        }
                        placeMarkers()
                    }
                })
    }

    private fun askPermission() {
        if  (ActivityCompat.checkSelfPermission(view?.context!!, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(view!!.context as Activity, Array<String>(2) {
                android.Manifest.permission.ACCESS_FINE_LOCATION
            }, 44)
    }

    private fun getCurrentLocation() {
        if  (view?.context?.let { ActivityCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    userPrefs.edit().putString("LOCATION_LAT", location.latitude.toString()).apply()
                    userPrefs.edit().putString("LOCATION_LON", location.longitude.toString()).apply()
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                    suppMap.getMapAsync(this)
                }
            }
        }
    }

    private fun placeMarkers() {
        if  (userPrefs.getString("LOCATION_LAT", "null") != "null" && userPrefs.getString("LOCATION_LON", "null") != "null") {
            val myLat = userPrefs.getString("LOCATION_LAT", "null")!!.toDouble()
            val myLong = userPrefs.getString("LOCATION_LON", "null")!!.toDouble()
            map.clear()
            myMarker = MarkerOptions()
            myMarker.position(LatLng(myLat, myLong))
                    .title("My location")
                    .icon(bitMapFromVector(R.drawable.ic_baseline_my_location_24))
            map.addMarker(myMarker)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(myMarker.position, 10f))
            for (i in 0 until dataset.size) {
                val latlng: LatLng = LatLng(dataset[i].lat, dataset[i].lng)
                val marker: MarkerOptions = MarkerOptions()
                marker.position(latlng)
                marker.title(dataset[i].name)
                map.addMarker(marker)
            }

            if (dataset.size == 0) {
                val noMovieTheaters: Toast = Toast.makeText(view?.context, "No nearby movie theaters", Toast.LENGTH_LONG)
                noMovieTheaters.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                noMovieTheaters.show()
            }
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0 != null) {
            map = p0
        }
        getPlaces()
    }

    private fun bitMapFromVector(vectorResID:Int):BitmapDescriptor {
        val vectorDrawable=ContextCompat.getDrawable(context!!,vectorResID)
        vectorDrawable!!.setBounds(0,0,vectorDrawable!!.intrinsicWidth,vectorDrawable.intrinsicHeight)
        val bitmap=Bitmap.createBitmap(vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
        val canvas=Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onDestroyView() {
        try{
            val fragment: Fragment? = fragmentManager!!.findFragmentById(R.id.map)
            val ft: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
            if (fragment != null) {
                ft.remove(fragment)
            }
            ft.commit()
        }catch(e: Exception) {
        }
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onDestroyView()
    }
}
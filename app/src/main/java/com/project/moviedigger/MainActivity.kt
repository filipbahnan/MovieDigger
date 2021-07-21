package com.project.moviedigger

import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.onesignal.OSPermissionSubscriptionState
import com.onesignal.OneSignal
import com.project.moviedigger.views.*
import org.json.JSONException


private const val TAG = "MainActivity"
class MainActivity: AppCompatActivity() {
    lateinit var nav : BottomNavigationView
    private lateinit var mapFragment: MapFragment
    private lateinit var upcomingFragment: UpcomingFragment
    private lateinit var releasedFragment: ReleasedFragment
    private lateinit var trailersFragment: TrailersFragment
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var userPrefs: SharedPreferences
    private var notification:Boolean = true
    private lateinit var notificationOn:MenuItem
    private lateinit var notificationOff:MenuItem

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_layout, menu)

        val status = OneSignal.getPermissionSubscriptionState()
        status.permissionStatus.enabled
        status.subscriptionStatus.subscribed

        notification = status.subscriptionStatus.subscribed

        notificationOn = menu?.findItem(R.id.notificationOn)!!
        notificationOff = menu?.findItem(R.id.notificationOff)!!

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.gps -> {
                mapFragment.show(supportFragmentManager, "MapFragment")
            }
            R.id.notificationOn -> {
                notification = false
                this.invalidateOptionsMenu()
                try {
                    val toast1 = Toast.makeText(this, "Notifications off", Toast.LENGTH_SHORT)
                    toast1.setGravity(Gravity.TOP or Gravity.LEFT, 50, 50)
                    toast1.show()
                    OneSignal.setSubscription(false)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            R.id.notificationOff -> {
                notification = true
                this.invalidateOptionsMenu()
                try {
                    val toast2 = Toast.makeText(this, "Notifications on", Toast.LENGTH_SHORT)
                    toast2.setGravity(Gravity.TOP or Gravity.LEFT, 50, 50)
                    toast2.show()
                    OneSignal.setSubscription(true)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        if (notification) {
            notificationOn.isVisible = true // show notification button
            notificationOff.isVisible = false // hide the notification off button
        } else if (!notification) {
            notificationOn.isVisible = false // hide notification button
            notificationOff.isVisible = true // show the notification off button
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userPrefs = this.getSharedPreferences("userdetails", MODE_PRIVATE)

        nav = findViewById(R.id.navigation)
        mapFragment = MapFragment()
        upcomingFragment = UpcomingFragment()
        releasedFragment = ReleasedFragment()
        trailersFragment = TrailersFragment()

        nav.setOnNavigationItemSelectedListener { item->
            selectFragment(item)
        }
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            UpcomingFragment()
        ).commit()
        askPermission()
        getCurrentLocation()

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()
    }

    private fun selectFragment(item: MenuItem): Boolean {
        var selectedFragment : Fragment? = null
        when (item.itemId) {
            R.id.nav_upcoming -> selectedFragment = upcomingFragment
            R.id.nav_released -> selectedFragment = releasedFragment
            R.id.nav_trailers -> selectedFragment = trailersFragment
        }

        if (selectedFragment != null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                selectedFragment
            ).addToBackStack(selectedFragment.tag).commit()
        }

        return true
    }

    private fun askPermission() {
        if  (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, Array<String>(2) {
                android.Manifest.permission.ACCESS_FINE_LOCATION
            }, 44)
    }

    private fun getCurrentLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if  (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    userPrefs.edit().putString("LOCATION_LAT", location.latitude.toString()).apply()
                    userPrefs.edit().putString("LOCATION_LON", location.longitude.toString()).apply()
                }
                else {
                    userPrefs.edit().putString("LOCATION_LAT", "null").apply()
                    userPrefs.edit().putString("LOCATION_LON", "null").apply()
                }
            }
        }
        else {
            userPrefs.edit().putString("LOCATION_LAT", "null").apply()
            userPrefs.edit().putString("LOCATION_LON", "null").apply()
        }
    }
}

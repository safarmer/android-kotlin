package com.example.safarmer.scratchapplication

import android.Manifest
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.example.safarmer.scratchapplication.data.DeviceViewModel
import com.example.safarmer.scratchapplication.view.DeviceAdapter
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.activity_main.drawer_layout as drawer
import kotlinx.android.synthetic.main.activity_main.nav_view as navigationView
import kotlinx.android.synthetic.main.content_main.device_list as deviceList
import kotlinx.android.synthetic.main.content_main.device_list_count as deviceListCount

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, LifecycleRegistryOwner {

  val lifecycleRegistry = LifecycleRegistry(this)
  override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

  private val deviceListAdapter = DeviceAdapter()
  private var deviceViewModel: DeviceViewModel? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel::class.java)

    fab.setOnClickListener {
      deviceViewModel?.toggle()
    }

    val toggle = ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer.addDrawerListener(toggle)
    toggle.syncState()

    navigationView.setNavigationItemSelectedListener(this)

    deviceList.setHasFixedSize(true)
    deviceList.adapter = deviceListAdapter
    deviceList.layoutManager = LinearLayoutManager(this)

    if (!checkPermissions()) {
      val perms = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH)
      ActivityCompat.requestPermissions(this, perms, 1)
    } else {
      watchDevices()
    }
  }

  fun checkPermissions(): Boolean {
    val locPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
    return (locPerm == PackageManager.PERMISSION_GRANTED)
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
      watchDevices()
    }
  }

  fun watchDevices() {
    deviceViewModel?.devices?.observe(this, Observer<Set<ScanResult>> {
      it?.let {
        deviceListAdapter.update(it)
        deviceListCount.text = "Devices Found: ${deviceListAdapter.size}"
      }
    })
  }

  override fun onBackPressed() {
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    when (item.itemId) {
      R.id.action_settings -> return true
      else -> return super.onOptionsItemSelected(item)
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    // Handle navigation view item clicks here.
    when (item.itemId) {
      R.id.nav_camera -> {
        // Handle the camera action
      }
      R.id.nav_gallery -> {

      }
      R.id.nav_slideshow -> {

      }
      R.id.nav_manage -> {

      }
      R.id.nav_share -> {

      }
      R.id.nav_send -> {

      }
    }

    drawer.closeDrawer(GravityCompat.START)
    return true
  }
}

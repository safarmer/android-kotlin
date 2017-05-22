package com.example.safarmer.scratchapplication.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.bluetooth.le.ScanResult

/** Represents a discovered bluetooth resultsData. */
class DeviceViewModel : ViewModel() {

  private val repo = DeviceRepository()

  val devices: LiveData<Set<ScanResult>> = repo.resultsData

  var running: Boolean = false
    private set

  override fun onCleared() {
    repo.stopScan()
    running = false
  }

  fun toggle() {
    if (running) {
      stop()
    } else {
      start()
    }
  }

  fun start() {
    repo.startScan()
    running = true
  }

  fun stop() {
    running = false
    repo.stopScan()
  }
}
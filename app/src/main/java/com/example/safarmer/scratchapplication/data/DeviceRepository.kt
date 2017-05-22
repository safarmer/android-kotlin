package com.example.safarmer.scratchapplication.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult

/** A Bluetooth device repository. */
class DeviceRepository {

  val scanner = BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner!!
  val scanResults: MutableSet<ScanResult> = mutableSetOf()
  val resultsData: MutableLiveData<Set<ScanResult>> = MutableLiveData()
  val scanCallback: ScanCallback

  init {
    scanCallback = object : ScanCallback() {
      override fun onScanResult(callbackType: Int, result: ScanResult) {
        if (result.isChromeboxForMeetings()) {
          return
        }
        scanResults.add(result)
        resultsData.value = scanResults
      }

      override fun onBatchScanResults(results: MutableList<ScanResult>) {
        scanResults.addAll(results.filterNot { it.isChromeboxForMeetings() })
        resultsData.value = scanResults
      }
    }
  }

  fun startScan(): LiveData<Set<ScanResult>> {
    scanner.startScan(scanCallback)
    return resultsData
  }

  fun stopScan() {
    scanner.stopScan(scanCallback)
  }

  fun ScanResult.isChromeboxForMeetings() = device.address.startsWith("5C")
}
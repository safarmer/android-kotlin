package com.example.safarmer.scratchapplication.view

import android.bluetooth.le.ScanResult
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.safarmer.scratchapplication.R

// liveScanResults: LiveData<Set<ScanResult>>
class DeviceAdapter : RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {

  private val devices: MutableMap<String, ScanResult> = linkedMapOf()

  val size: Int get() = devices.size

  fun update(results: Set<ScanResult>) {
    devices.putAll(results.map({ it.device.address to it }))
    notifyDataSetChanged()
  }

  override fun onBindViewHolder(viewHolder: DeviceAdapter.ViewHolder, position: Int) {
    viewHolder.bind(devices.values.elementAt(position))
  }

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var deviceAddress = itemView.findViewById(R.id.device_address) as TextView
    private var deviceName = itemView.findViewById(R.id.device_name) as TextView
    private var deviceRssi = itemView.findViewById(R.id.device_rssi) as TextView

    fun bind(scanResult: ScanResult) {
      deviceAddress.text = scanResult.device.address
      deviceName.text = scanResult.scanRecord.deviceName ?: "<unknown>"
      deviceRssi.text = "RSSI: ${scanResult.rssi}"
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, position: Int)
      = ViewHolder(parent.inflate(R.layout.device_item))

  override fun getItemCount(): Int = devices.size
}

fun ViewGroup.inflate(resourceId: Int): View {
  return LayoutInflater.from(context).inflate(resourceId, this, false)
}

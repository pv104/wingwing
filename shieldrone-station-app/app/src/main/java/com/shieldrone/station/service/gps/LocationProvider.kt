package com.shieldrone.station.service.gps

interface LocationProvider {
    fun startLocationUpdates(onLocationReceived: (latitude: Double, longitude: Double) -> Unit)
    fun stopLocationUpdates()
}
package net.level3studios.astro.models

import android.database.Cursor
import android.location.Location
import com.google.android.gms.maps.model.LatLng

class AstroLocation() {
    var id: Int = 0
    var city: String = ""
    var country: String = ""
    var timeZone: String = ""
    var coordinates: LatLng = LatLng(0.0,0.0)

    companion object {
        fun defaultLocation(): AstroLocation {
            val sanFran = AstroLocation()
            sanFran.id = 297
            sanFran.city = "San Francisco"
            sanFran.country = "United States"
            sanFran.timeZone = "America/Los_Angeles"
            sanFran.coordinates = LatLng(37.7749295, -122.4194183)
            return sanFran
        }
    }

    enum class Columns(var label: String) {
        ID("id"),
        CITY("city"),
        COUNTRY("country"),
        LATITUDE("latitude"),
        LONGITUDE("longitude"),
        TIMEZONE("timezone")
    }

    constructor(row: Cursor) : this() {
        this.id = row.getInt(row.getColumnIndex(Columns.ID.label))
        this.city = row.getString(row.getColumnIndex(Columns.CITY.label))
        this.country = row.getString(row.getColumnIndex(Columns.COUNTRY.label))
        this.timeZone = row.getString(row.getColumnIndex(Columns.TIMEZONE.label))
        val lat = row.getDouble(row.getColumnIndex(Columns.LATITUDE.label))
        val lng = row.getDouble(row.getColumnIndex(Columns.LONGITUDE.label))
        this.coordinates = LatLng(lat, lng)
    }
}
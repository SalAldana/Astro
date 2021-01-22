//
//  WorldCity.swift
//  Astro
//
//  Created by Aldana, Sal on 1/19/21.
//

import Foundation
import GRDB
import MapKit

//MARK: - Locations Table -
class AstroLocation: Record, Identifiable {
    var id: Int
    var city: String
    var country: String
    var timezone: String
    var coordinates: CLLocationCoordinate2D
    var location: CLLocation {
        return CLLocation(latitude: coordinates.latitude, longitude: coordinates.longitude)
    }
    
    override class var databaseTableName: String {
        return "locations"
    }
    
    enum Columns: String, ColumnExpression {
        case id, country, city, latitude, longitude, timezone
    }
    
    required init(row: Row) {
        self.id = row[Columns.id]
        self.city = row[Columns.city]
        self.country = row[Columns.country]
        self.timezone = row[Columns.timezone]
        let lat: Double = row[Columns.latitude]
        let lng: Double = row[Columns.longitude]
        self.coordinates = CLLocationCoordinate2D(latitude: lat, longitude: lng)
        super.init(row: row)
    }
    
    init(id: Int, city: String, country: String, timeZone: String, lat: Double, lng: Double) {
        self.id = id
        self.city = city
        self.country = country
        self.timezone = timeZone
        self.coordinates = CLLocationCoordinate2D(latitude: lat, longitude: lng)
        super.init()
    }
    
    static var defaultLocation: AstroLocation {
        return AstroLocation(id: 297,
                             city: "San Francisco",
                             country: "United States",
                             timeZone: "America/Los_Angeles",
                             lat: 37.7749295,
                             lng: -122.4194183)
    }
    
    static var defaultSpan: MKCoordinateSpan {
        return MKCoordinateSpan(latitudeDelta: 12,
                                longitudeDelta: 12)
    }
    
    static var defaultRegion: MKCoordinateRegion {
        return MKCoordinateRegion(center: AstroLocation.defaultLocation.coordinates,
                                  span: AstroLocation.defaultSpan)
    }
}

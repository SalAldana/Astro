//
//  LocationHelper.swift
//  Astro
//
//  Created by Aldana, Sal on 1/19/21.
//

import Foundation
import GRDB

class LocationHelper {
    
    var databaseQueue: DatabaseQueue?
    var allLocations: [AstroLocation]?
    
    func setupDatabase() {
        guard let path = Bundle.main.path(forResource: "worldcities", ofType: "db") else { return }
        self.databaseQueue = try? DatabaseQueue(path: path)
        self.getAllLocations()
    }
    
    private func getAllLocations() {
        DispatchQueue.global(qos: .background).async {
            let locations = self.databaseQueue?.read({ database -> [AstroLocation]? in
                let places = try? AstroLocation.fetchAll(database)
                return places
            })
            self.allLocations = locations
        }
    }
    
    func getLocation(fromTimeZone timeZone: String) -> AstroLocation? {
        let location = databaseQueue?.read({ database -> AstroLocation? in
            let place = try? AstroLocation
                .filter(AstroLocation.Columns.timezone == timeZone)
                .fetchOne(database)
            return place
        })
        return location
    }
    
}

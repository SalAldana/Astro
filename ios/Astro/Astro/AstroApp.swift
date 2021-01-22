//
//  AstroApp.swift
//  Astro
//
//  Created by Aldana, Sal on 1/18/21.
//

import SwiftUI

@main
struct AstroApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView(databaseHelper: helper)
        }
    }
    
    var helper: LocationHelper {
        let helper = LocationHelper()
        helper.setupDatabase()
        return helper
    }
}

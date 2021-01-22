//
//  AstroViewModel.swift
//  Astro
//
//  Created by Aldana, Sal on 1/18/21.
//

import Foundation
import SwiftUI
import SwiftDate
import MapKit
import suncalc
import EKAstrologyCalc

class AstroViewModel: ObservableObject {
    
    enum Phase {
        case sunrise
        case noon
        case sunset
        case night
        
        func isNightPhase() -> Bool {
            return self == .night
        }
    }
    
    @Published var viewPhase: Phase = .noon
    @Published var viewLocation: AstroLocation = AstroLocation.defaultLocation
    @Published var viewDate: Date = Date()
    @Published var region: MKCoordinateRegion = AstroLocation.defaultRegion
    
    var locationHelper: LocationHelper?
    
    //MARK: - UI Tools -
    func backgroundColor() -> LinearGradient {
        switch self.viewPhase {
        case .noon:
            return DayColors.backgroundGradient
        case .night:
            return NightColors.backgroundGradient
        default:
            return PeakColors.backgroundGradient
        }
    }
    
    func primaryColor() -> Color {
        switch self.viewPhase {
        case .noon:
            return DayColors.fuzzyWuzzyBrown
        case .night:
            return NightColors.portGore
        default:
            return PeakColors.stilleto
        }
    }
    
    func secondaryColor() -> Color {
        switch self.viewPhase {
        case .noon:
            return DayColors.solidPink
        case .night:
            return NightColors.blueViolet
        default:
            return PeakColors.martinique
        }
    }
    
    func cityImage() -> Image {
        switch self.viewPhase {
        case .noon:
            return Image("Day Scene")
        case .night:
            return Image("Night Scene")
        default:
            return Image("Sunset Scene")
        }
    }
    
    
    //MARK: - Phase Functions -
    func togglePhase() {
        if self.viewPhase.isNightPhase() {
            self.viewPhase = .noon
        } else {
            self.viewPhase =  .night
        }
    }
    
    var textColor: Color {
        switch self.viewPhase {
        case .noon:
            return DayColors.pickledBluewood
        case .night:
            return NightColors.waterloo
        default:
            return PeakColors.wafer
        }
    }
    
    var phaseDisplay: String {
        switch self.viewPhase {
        case .sunrise:
            return "SUNRISE"
        case .noon:
            return "NOON"
        case .sunset:
            return "SUNSET"
        case .night:
            return self.getMoonPhase()
        }
    }
    
    var phaseSubtitle: String {
        if !self.viewPhase.isNightPhase() {
            return self.getSunTime(forPhase: self.viewPhase)
        } else {
            return self.getMoonIllumination()
        }
    }
    
    //MARK: - Date Function -
    var getDateRange: ClosedRange<Date> {
        let today = Date()
        let pastDate = today.dateByAdding(-4, .year).date
        let futureDate = today.dateByAdding(4, .year).date
        return pastDate...futureDate
    }
    
    //MARK: - Location Functions -
    var allLocations: [AstroLocation] {
        return locationHelper?.allLocations ?? []
    }
    
    var locationButtonText: String {
        let city = self.viewLocation.city
        let country = self.viewLocation.country
        return "\(city), \(country)"
    }
    
    //MARK: - SunCalc -
    func getSunTime(forPhase phase: Phase) -> String {
        var phaseDate: Date
        let sunTimes = SunCalc.getTimes(date: self.viewDate, latitude: self.viewLocation.coordinates.latitude, longitude: self.viewLocation.coordinates.longitude)
        switch phase {
        case .sunset:
            phaseDate = sunTimes.sunset
        case .sunrise:
            phaseDate = sunTimes.sunrise
        default:
            phaseDate = sunTimes.solarNoon
        }
        return getDateString(fromDate: phaseDate)
    }
    
    internal func getDateString(fromDate date: Date) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.timeZone = TimeZone(identifier: self.viewLocation.timezone)
        dateFormatter.timeStyle = .short
        let dateString = dateFormatter.string(from: date)
        return "@ \(dateString)"
    }
    
    //MARK - Moon Calc -
    func getMoonPhase() -> String {
        let calc = EKAstrologyCalc(location: self.viewLocation.location)
        let info = calc.getInfo(date: self.viewDate)
        switch info.phase {
        case .firstQuarter:
            return "FIRST QUARTER"
        case .fullMoon:
            return "FULL MOON"
        case .lastQuarter:
            return "LAST QUARTER"
        case .newMoon:
            return "NEW MOON"
        case .waningCrescent:
            return "WANING CRESCENT"
        case .waningGibbous:
            return "WANING GIBBOUS"
        case .waxingCrescent:
            return "WAXING CRESCENT"
        case .waxingGibbous:
            return "WAXING GIBBOUS"
        }
    }
    func getMoonIllumination() -> String {
        let calc = EKAstrologyCalc(location: self.viewLocation.location)
        let info = calc.getInfo(date: self.viewDate)
        let illumination = info.illumination?.fraction ?? 0
        return getIlluminationString(fromValue: illumination)
    }
    
    internal func getIlluminationString(fromValue fraction: Double) -> String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .percent
        let percent = formatter.string(from: NSNumber(value: fraction)) ?? "0%"
        return "\(percent) Illumination"
    }
}

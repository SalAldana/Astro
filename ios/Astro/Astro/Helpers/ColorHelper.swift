//
//  ColorHelper.swift
//  Astro
//
//  Created by Aldana, Sal on 1/18/21.
//

import Foundation
import UIKit
import Hue
import SwiftUI

enum DayColors {
    static let fuzzyWuzzyBrown = Color(UIColor(hex: "#C35F53"))
    static let solidPink = Color(UIColor(hex: "#903749"))
    static let pickledBluewood = Color(UIColor(hex: "#353B5C"))
    static let graySuit = Color(UIColor(hex: "#B9BACA"))
    static let gradientStart = Color(UIColor(hex: "#8694B7"))
    static let gradientEnd = Color(UIColor(hex: "#E2EAF4"))
    static let backgroundGradient = LinearGradient(gradient: Gradient(colors: [Self.gradientStart, Self.gradientEnd]), startPoint: .top, endPoint: .bottom)
}

enum PeakColors {
    static let bouquet = Color(UIColor(hex: "#AC6F9F"))
    static let martinique = Color(UIColor(hex: "#313151"))
    static let stilleto = Color(UIColor(hex: "#982F3C"))
    static let wafer = Color(UIColor(hex: "#E3D3CA"))
    static let gradientStart = Color(UIColor(hex: "#8360A8"))
    static let gradientEnd = Color(UIColor(hex: "#FFA79F"))
    static let backgroundGradient = LinearGradient(gradient: Gradient(colors: [Self.gradientStart, Self.gradientEnd]), startPoint: .top, endPoint: .bottom)
}

enum NightColors {
    static let portGore = Color(UIColor(hex: "#292658"))
    static let turkistRose = Color(UIColor(hex: "#BA7183"))
    static let blueViolet = Color(UIColor(hex: "#6167B6"))
    static let waterloo = Color(UIColor(hex: "#7E849C"))
    static let gradientStart = Color(UIColor(hex: "#13103B"))
    static let gradientEnd = Color(UIColor(hex: "#D6737A"))
    static let backgroundGradient = LinearGradient(gradient: Gradient(colors: [Self.gradientStart, Self.gradientEnd]), startPoint: .top, endPoint: .bottom)
}

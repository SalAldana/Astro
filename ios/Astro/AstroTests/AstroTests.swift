//
//  AstroTests.swift
//  AstroTests
//
//  Created by Aldana, Sal on 1/18/21.
//

import XCTest
@testable import Astro

class AstroTests: XCTestCase {
    
    var viewModel: AstroViewModel?

    override func setUpWithError() throws {
        // Put setup code here. This method is called before the invocation of each test method in the class.
        self.viewModel = AstroViewModel()
    }

    override func tearDownWithError() throws {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        self.viewModel = nil
    }

    func testPhaseToggle() {
        XCTAssertTrue(viewModel?.viewPhase == .noon)
        viewModel?.togglePhase()
        XCTAssertTrue(viewModel?.viewPhase == .night)
    }
    
    func testPhaseTitle() {
        viewModel?.viewPhase = .noon
        XCTAssertTrue(viewModel?.phaseDisplay == "NOON")
        viewModel?.viewPhase = .sunrise
        XCTAssertTrue(viewModel?.phaseDisplay == "SUNRISE")
        viewModel?.viewPhase = .sunset
        XCTAssertTrue(viewModel?.phaseDisplay == "SUNSET")
        viewModel?.viewPhase = .night
        XCTAssertNotNil(viewModel?.phaseDisplay)
    }
    
    func testDateSubtitle() {
        let calendar = Calendar(identifier: .gregorian)
        var components = DateComponents()
        components.hour = 4
        components.minute = 20
        components.timeZone = TimeZone(identifier: viewModel?.viewLocation.timezone ?? "")
        let testDate = calendar.date(from: components) ?? Date()
        let dateString = viewModel?.getDateString(fromDate: testDate)
        XCTAssertEqual(dateString, "@ 4:20 AM")
    }
    
    func testPercentSubtitle() {
        let value = 0.13
        let numberString = viewModel?.getIlluminationString(fromValue: value)
        XCTAssertEqual(numberString, "13% Illumination")
    }
    
    func testLocationButtonText() {
        let locationText = viewModel?.locationButtonText
        XCTAssertTrue(locationText == "San Francisco, United States")
    }

}

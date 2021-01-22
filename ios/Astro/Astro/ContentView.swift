//
//  ContentView.swift
//  Astro
//
//  Created by Aldana, Sal on 1/18/21.
//

import SwiftUI

struct ContentView: View {
    var databaseHelper: LocationHelper?
    var body: some View {
        AstroView(viewModel: viewModel)
    }
    
    var viewModel: AstroViewModel {
        let model = AstroViewModel()
        model.locationHelper = databaseHelper
        return model
    }
}

// MARK: - UI Builder -
struct AstroView: View {
    @ObservedObject var viewModel = AstroViewModel()
    var body: some View {
        VStack {
            TopBar(viewModel: viewModel)
            Spacer()
            PhaseView(viewModel: viewModel)
            InforArea(viewModel: viewModel)
            Spacer()
            CityView(viewModel: viewModel)
            Spacer()
            ButtonArea(viewModel: viewModel)
            Spacer()
        }.background(self.viewModel.backgroundColor())
        .edgesIgnoringSafeArea(.all)
    }
}

// MARK: - Top Bar -
struct TopBar: View {
    @ObservedObject var viewModel: AstroViewModel
    @State private var showSettings: Bool = false
    
    var body: some View {
        HStack {
            Button(action: {
                self.viewModel.togglePhase()
            }, label: {
                AppImages.moonImage
                    .padding()
                    .foregroundColor(viewModel.secondaryColor())
            })
            Spacer()
            Button(action: {
                self.showSettings = true
            }, label: {
                AppImages.gearImage
                    .padding()
                    .foregroundColor(viewModel.secondaryColor())
            })
        }
        .statusBar(hidden: true)
        .padding(.top, 16)
        .sheet(isPresented: $showSettings, content: {
            SettingsView(viewModel: viewModel)
        })
    }
}

//MARK: - Phase View -
struct PhaseView: View {
    @ObservedObject var viewModel: AstroViewModel
    @State private var showPhasePicker = false
    
    var body: some View {
        Group {
            if viewModel.viewPhase.isNightPhase() {
                AnimatedMoonView()
                    .onTapGesture {
                        self.didTapPhase()
                    }
            } else {
                AnimatedSunView()
                    .onTapGesture {
                        self.didTapPhase()
                    }
            }
        }
        .actionSheet(isPresented: $showPhasePicker, content: {
            ActionSheet(title: Text("Select a Phase"),
                        message: nil,
                        buttons: [
                            .default(Text("SUNRISE"), action: { viewModel.viewPhase = .sunrise}),
                            .default(Text("NOON"), action: { viewModel.viewPhase = .noon}),
                            .default(Text("SUNSET"), action: { viewModel.viewPhase = .sunset}),
                            .cancel()
                        ])
        })
    }
    
    func didTapPhase() {
        if !viewModel.viewPhase.isNightPhase() {
            self.showPhasePicker = true
        }
    }
}

//MARK: - Info Area -
struct InforArea: View {
    @ObservedObject var viewModel: AstroViewModel
    
    var body: some View {
        VStack {
            Text(viewModel.phaseDisplay)
                .font(.largeTitle)
                .fontWeight(.bold)
                .foregroundColor(viewModel.textColor)
            Text(viewModel.phaseSubtitle)
                .font(.title2)
                .foregroundColor(viewModel.textColor)
            
        }
    }
}

//MARK: - City View -
struct CityView: View {
    @ObservedObject var viewModel: AstroViewModel
    
    var body: some View {
        viewModel.cityImage()
            .resizable()
            .scaledToFit()
    }
}

//MARK: - Button Area -
struct ButtonArea: View {
    @Environment(\.colorScheme) var colorScheme
    @ObservedObject var viewModel: AstroViewModel
    @State private var showLocationPicker: Bool = false
    
    var body: some View {
        VStack(alignment: .center, spacing: 12) {
            Button(action: {
                self.showLocationPicker = true
            }, label: {
                HStack {
                    Text(viewModel.locationButtonText)
                    Spacer()
                    AppImages.mapImage
                }.padding()
            })
            .foregroundColor(.white)
            .background(viewModel.primaryColor())
            .cornerRadius(6.0)
            .overlay(RoundedRectangle(cornerRadius: 6.0).stroke(viewModel.primaryColor(), lineWidth: 1))
            HStack {
                DatePicker(selection: $viewModel.viewDate, in: viewModel.getDateRange, displayedComponents: .date, label: {
                    Text("")
                }).fixedSize()
                .labelsHidden()
                .padding(.leading, 4)
                .colorMultiply(.black)
                .colorInvert()
                .accentColor(viewModel.primaryColor())
                Spacer()
                AppImages.calendarImage
                    .foregroundColor(.white)
                    .padding()
            }
            .background(viewModel.primaryColor())
            .cornerRadius(6.0)
            .overlay(RoundedRectangle(cornerRadius: 6.0).stroke(viewModel.primaryColor(), lineWidth: 1))
        }.padding([.leading, .trailing], 22)
        .sheet(isPresented: $showLocationPicker, content: {
            let locations = viewModel.allLocations
            MapView(viewModel: viewModel, mapLocations: locations)
        })
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

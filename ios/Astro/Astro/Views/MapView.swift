//
//  MapView.swift
//  Astro
//
//  Created by Aldana, Sal on 1/20/21.
//

import Foundation
import SwiftUI
import MapKit
import SlideOverCard

struct MapView: View {
    @Environment(\.presentationMode) var presentationMode
    @ObservedObject var viewModel: AstroViewModel
    @State var tracking: MapUserTrackingMode = .none
    @State private var detailPosition = CardPosition.bottom
    @State private var detailBackground = BackgroundStyle.blur
    @State private var selectedLocation: AstroLocation?
    var mapLocations: [AstroLocation]
    
    var body: some View {
        NavigationView {
            ZStack(alignment: .top) {
                mapView
                slideView
            }
            .navigationBarTitle(Text("Locations"), displayMode: .inline)
            .navigationBarItems(leading: cancelButton)
            .foregroundColor(viewModel.primaryColor())
            .edgesIgnoringSafeArea(.vertical)
        }
    }
    
    var mapView: some View {
        Map(coordinateRegion: $viewModel.region,
            interactionModes: .all,
            showsUserLocation: false,
            userTrackingMode: $tracking,
            annotationItems: mapLocations,
            annotationContent: { location in
                MapAnnotation(coordinate: location.coordinates, content: {
                    Button(action: {
                        selectedLocation = location
                        detailPosition = .middle
                    }, label: {
                        AppImages.mapPinImage
                            .resizable()
                            .frame(width: 25, height: 25, alignment: .center)
                            .foregroundColor(viewModel.primaryColor())
                    })
                })
            })
    }
    
    var slideView: some View {
        SlideOverCard($detailPosition, backgroundStyle: $detailBackground) {
            VStack(alignment: .center, spacing: 12) {
                Text("Location Details").font(.title2).bold().foregroundColor(.black)
                HStack {
                    Text("City:").foregroundColor(.black)
                    Text(selectedLocation?.city ?? "").italic()
                }
                HStack {
                    Text("Country:").foregroundColor(.black)
                    Text(selectedLocation?.country ?? "").italic()
                }
                HStack {
                    Text("TimeZone:").foregroundColor(.black)
                    Text(selectedLocation?.timezone ?? "").italic()
                }
                HStack {
                    Text("Coordinates: ").foregroundColor(.black)
                    Text("\(selectedLocation?.coordinates.latitude ?? 0), \(selectedLocation?.coordinates.longitude ?? 0)")
                }
                Button(action: {
                    if let location = selectedLocation {
                        viewModel.viewLocation = location
                    }
                    presentationMode.wrappedValue.dismiss()
                }, label: {
                    Text("Update Location")
                        .padding()
                        .foregroundColor(.white)
                        .background(viewModel.primaryColor())
                        .cornerRadius(6.0)
                        .overlay(RoundedRectangle(cornerRadius: 6.0).stroke(viewModel.primaryColor(), lineWidth: 1))
                })
            }
        }
    }
    
    var cancelButton: some View {
        Button(action: {
            self.presentationMode.wrappedValue.dismiss()
        }, label: {
            Text("Cancel")
        })
    }
}

struct MapView_Preivew: PreviewProvider {
    static var previews: some View {
        MapView(viewModel: AstroViewModel(), mapLocations: [AstroLocation.defaultLocation])
    }
}

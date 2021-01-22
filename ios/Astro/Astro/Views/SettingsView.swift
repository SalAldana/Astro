//
//  SettingsView.swift
//  Astro
//
//  Created by Aldana, Sal on 1/21/21.
//

import Foundation
import SwiftUI

struct SettingsView: View {
    
    @Environment(\.presentationMode) var presentationMode
    @ObservedObject var viewModel: AstroViewModel
    
    var body: some View {
        VStack(alignment: .center, spacing: 12) {
            Text("Thanks for checking out Astro 😎")
                .font(.headline)
                .padding(.top, 26)
            Button(action: {
                viewModel.viewDate = Date()
                viewModel.viewLocation = AstroLocation.defaultLocation
                viewModel.viewPhase = .noon
                presentationMode.wrappedValue.dismiss()
            }, label: {
                Text("Reset Astro")
                    .padding()
                    .background(viewModel.primaryColor())
                    .foregroundColor(.white)
                    .cornerRadius(6.0)
            })
            Spacer()
        }
    }
}

struct Settings_Preview: PreviewProvider {
    static var previews: some View {
        SettingsView(viewModel: AstroViewModel())
    }
}


//
//  LottieView.swift
//  Astro
//
//  Created by Aldana, Sal on 1/18/21.
//

import Foundation
import SwiftUI
import Lottie
import SnapKit


struct AnimatedSunView: View {
    
    var body: some View {
        LottieView(animationName: "1173-sun-burst-weather-icon")
            .frame(width: 125.0, height: 125.0)
    }
}

struct AnimatedMoonView: View {
    
    var body: some View {
        LottieView(animationName: "36246-moon-icon")
            .frame(width: 125.0, height: 125.0)
    }
}

struct LottieView: UIViewRepresentable {
    typealias UIViewType = UIView
    var animationName: String
    
    func makeUIView(context: Context) -> UIView {
        let view = UIView(frame: .zero)
        
        let animatedView = AnimationView()
        let animated = Animation.named(animationName)
        animatedView.animation = animated
        animatedView.contentMode = .scaleAspectFill
        animatedView.loopMode = .loop
        animatedView.play()
        
        view.addSubview(animatedView)
        animatedView.snp.makeConstraints({ make in
            make.edges.equalToSuperview()
        })
        
        return view
    }
    
    func updateUIView(_ uiView: UIView, context: Context) {
        
    }
    
}

//
//  PopupHandler.swift
//  finalGanttProject
//
//  Created by Gregory Fournier on 2018-05-28.
//  Copyright © 2018 Gregory Fournier. All rights reserved.
//

import Foundation
import PopupDialog

class PopupHandler {
    
    public static func Popup(_ title: String, _ message: String, _ viewController: UIViewController ) {
        // Create the dialog
        let popup = PopupDialog(title: title, message: message)
        
        // Create button
        let buttonOne = DefaultButton(title: "OK") {
        }
        
        // Add Button
        popup.addButton(buttonOne);
        
        // Present dialog
        viewController.present(popup, animated: true, completion: nil)
    }
}

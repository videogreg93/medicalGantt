//
//  databaseManager.swift
//  finalGanttProject
//
//  Created by Gregory Fournier on 2018-02-02.
//  Copyright © 2018 Gregory Fournier. All rights reserved.
//

import Foundation
import FirebaseDatabase

class DatabaseManager {
    static var ref: DatabaseReference?;
    public static var urgenceArray:[Urgence] = [];
    
    static func initialize() {
        ref = Database.database().reference().child("urgence");
        ref?.observe(DataEventType.childAdded, with: { (snapshot) in
            let entry = snapshot.value as? NSDictionary
            // Create urgence
            let arrivalTime: String = entry!["Arrival Time"] as! String;
            let doctorName: String = entry!["Doctor Name"] as! String;
            let duration: Int = ((entry!["Duration"] as! NSString).integerValue);
            let operationType: String = entry!["Operation Type"] as! String;
            let timeToBeginOperation: Int = (entry!["Time to begin Operation"] as! NSString).integerValue;
            urgenceArray.append(Urgence(arrivalTime,doctorName,duration,operationType,timeToBeginOperation))
        
            // Tell spreadsheet to reload the data on screen
            ViewController.viewController?.spreadsheetView.reloadData();
        })
        
    }
    
}

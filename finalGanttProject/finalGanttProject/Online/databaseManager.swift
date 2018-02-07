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
        // Child added listener
        ref?.observe(DataEventType.childAdded, with: { (snapshot) in
            let entry = snapshot.value as? NSDictionary
            // Create urgence
            let id: String = snapshot.key;
            let arrivalTime: Time = Urgence.convertArrivalTimeFromOnline(entry!["Arrival Time"] as! NSDictionary);
            let doctorName: String = entry!["Doctor Name"] as! String;
            let duration: Int = ((entry!["Duration"] as! NSString).integerValue);
            let operationType: String = entry!["Operation Type"] as! String;
            let timeToBeginOperation: Int = (entry!["Time to begin Operation"] as! NSString).integerValue;
            let date: CustomDate = Urgence.convertArrivalDateFromOnline(entry!["Arrival Date"] as! NSDictionary);
            urgenceArray.append(Urgence(arrivalTime,doctorName,duration,
                                        operationType,timeToBeginOperation, id, date));
            
           
        
            // Tell spreadsheet to reload the data on screen
            ViewController.viewController?.spreadsheetView.reloadData();
        })
        
        ref?.observe(DataEventType.childRemoved, with: { (snapshot) in
            //let entry = snapshot.value as? NSDictionary
            // remove child from urgence array
            removeFromUrgenceArray(snapshot.key);
            
            // Tell spreadsheet to reload the data on screen
            ViewController.viewController?.spreadsheetView.reloadData();
        })
        
    }
    
    private static func removeFromUrgenceArray(_ id: String) {
        for i in 0...urgenceArray.count-1 {
            if (urgenceArray[i]._id == id) {
                urgenceArray.remove(at: i);
                return;
            }
        }
    }
    
}

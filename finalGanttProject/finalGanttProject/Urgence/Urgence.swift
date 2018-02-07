//
//  Urgence.swift
//  finalGanttProject
//
//  Created by Gregory Fournier on 2018-02-02.
//  Copyright © 2018 Gregory Fournier. All rights reserved.
//

import Foundation

class Urgence {
    var _id: String;
    var arrivalTime: Time;
    var doctorName: String;
    var duration: Int;
    var operationType: String;
    var timeToBeginOperation: Int;
    
    init(_ arrivalTime: Time, _ doctorName: String,
         _ duration: Int, _ operationType: String   ,
         _ timeToBeginOperation: Int, _ id: String) {
        self.arrivalTime = arrivalTime;
        self.doctorName = doctorName;
        self.duration = duration;
        self.operationType = operationType;
        self.timeToBeginOperation = timeToBeginOperation;
        self._id = id;
        
    }
    
    //MARK: Static functions
    
    public static func convertArrivalTimeFromOnline(_ time: NSDictionary) -> Time {
        let hour: Int = time.value(forKey: "hour") as! Int;
        let minute: Int = time.value(forKey: "minute") as! Int
        
        
        return Time(hour, minute);
        
    }
    
    public func toString() -> String {
        var string: String = "\nArrival Time: " + arrivalTime.description;
        string = "_id: " + _id + string;
        string += "\nDoctor: " + doctorName;
        string += "\nDuration: " + String(duration);
        string += "\nOperation Type: " + operationType;
        string += "\nTime to begin operation: " + String(timeToBeginOperation) + "\n";
        return string;
    }
    
    /*required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }*/
}

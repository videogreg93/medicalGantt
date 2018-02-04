//
//  Urgence.swift
//  finalGanttProject
//
//  Created by Gregory Fournier on 2018-02-02.
//  Copyright © 2018 Gregory Fournier. All rights reserved.
//

import Foundation

class Urgence {
    var arrivalTime: String;
    var doctorName: String;
    var duration: Int;
    var operationType: String;
    var timeToBeginOperation: Int;
    
    init(_ arrivalTime: String, _ doctorName: String,
         _ duration: Int, _ operationType: String   ,
         _ timeToBeginOperation: Int) {
        self.arrivalTime = arrivalTime;
        self.doctorName = doctorName;
        self.duration = duration;
        self.operationType = operationType;
        self.timeToBeginOperation = timeToBeginOperation;
        
    }
    
    public func toString() -> String {
        var string: String = "Arrival Time: " + arrivalTime;
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

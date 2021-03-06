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
    var arrivalDate: CustomDate;
    var doctorName: String;
    var duration: Int;
    var operationType: String;
    var timeToBeginOperation: Int;
    var dossier: Int;
    
    init(_ arrivalTime: Time, _ doctorName: String,
         _ duration: Int, _ operationType: String   ,
         _ timeToBeginOperation: Int, _ id: String, _ date: CustomDate, _ dos: Any!) {
        self.arrivalTime = arrivalTime;
        self.doctorName = doctorName;
        self.duration = duration;
        self.operationType = operationType;
        self.timeToBeginOperation = timeToBeginOperation;
        self._id = id;
        self.arrivalDate = date;
        do {
            self.dossier = (try Int(Encryption.decrypt(dos as! String)))!;
        } catch {
            self.dossier = -1;
            print(error)
        }
        
    }
    
    //MARK: Static functions
    
    public static func convertArrivalTimeFromOnline(_ time: NSDictionary) -> Time {
        let hour: Int = time.value(forKey: "hour") as! Int;
        let minute: Int = time.value(forKey: "minute") as! Int
        return Time(hour, minute);
    }
    
    public static func convertArrivalDateFromOnline(_ date: NSDictionary) -> CustomDate {
        let day: Int = date.value(forKey: "day") as! Int;
        let month: Int = date.value(forKey: "month") as! Int;
        let year: Int = date.value(forKey: "year") as! Int;
        return CustomDate(day,month,year);
    }
    
    public func toString() -> String {
        var string: String = "\nHeure d'arrivée: " + arrivalTime.description;
        string += "\n# de dossier: " + String(dossier);
        //string += "\nRounded Time: " + String(arrivalTime.getRoundedTime());
        string += "\nDate d'arrivée: " + arrivalDate.description;
        //string = "_id: " + _id + string;
        string += "\nMédecin: " + doctorName;
        string += "\nChirurgie Planifiée: " + operationType;
        string += "\nTemps de latence acceptable: " + String(timeToBeginOperation);
        string += "\nDurée prévue de l'opération: " + String(duration) + "\n";
        return string;
    }
    
    /*required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }*/
}

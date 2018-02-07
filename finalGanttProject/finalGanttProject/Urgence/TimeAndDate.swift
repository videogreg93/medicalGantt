//
//  TimeAndDate.swift
//  finalGanttProject
//
//  Created by Gregory Fournier on 2018-02-07.
//  Copyright © 2018 Gregory Fournier. All rights reserved.
//

import Foundation

/*
 Util functions for simple time and date data
 */

class Time: CustomStringConvertible {
    var hour: Int = 0;
    var minute: Int = 0;
    
    init(_ hour: Int, _ minute: Int) {
        self.hour = hour;
        self.minute = minute;
    }
    
    // Return the time rounded to closes hour divided by 4
    // ex return 0 when 0 <= hour < 2. return 1 when 2 <= hour < 6
    public func getRoundedTime() -> Int {
        var temp = hour;
        if minute >= 30 {
            temp += 1;
        }
        
        if hour % 4 >= 2 {
            return (temp/4)+1
        }
        
        return temp/4;
    }
    
    public var description: String {
        return (String(hour) + ":" + String(minute));
    }
}

class CustomDate: CustomStringConvertible {
    var day: Int = 0;
    var month: Int = 1; // 1 indexed
    var year: Int = 0;
    
    public var description: String {
        return (String(day) + "/" + String(month) + "/" + String(year));
    }
}

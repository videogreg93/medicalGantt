//
//  databaseTests.swift
//  finalGanttProjectTests
//
//  Created by Gregory Fournier on 2018-02-07.
//  Copyright © 2018 Gregory Fournier. All rights reserved.
//

import XCTest

class databaseTests: XCTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override class func setUp() {
        super.setUp()
        // Called once before all tests are run
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func roundingTime() {
        for i in 0...24 {
            let time: Time = Time(i,0);
            XCTAssertEqual(time.getRoundedTime(), i/4)
        }
        
        var time: Time = Time(2,35)
        XCTAssertEqual(time.getRoundedTime(), 1)
        
        time = Time(5,3)
        XCTAssertEqual(time.getRoundedTime(), 1)

        
    }
    

    
}

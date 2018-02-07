//
//  finalGanttProjectTests.swift
//  finalGanttProjectTests
//
//  Created by Gregory Fournier on 2018-02-07.
//  Copyright © 2018 Gregory Fournier. All rights reserved.
//

import XCTest

class finalGanttProjectTests: XCTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testExample() {
       
        var time: Time = Time(2,35)
        XCTAssertEqual(time.getRoundedTime(), 1)
        time = Time(5,3)
        XCTAssertEqual(time.getRoundedTime(), 1)
    }
    
    func testPerformanceExample() {
        // This is an example of a performance test case.
        self.measure {
            // Put the code you want to measure the time of here.
        }
    }
    
}

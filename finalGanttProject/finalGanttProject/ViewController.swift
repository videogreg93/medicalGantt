//
//  ViewController.swift
//  finalGanttProject
//
//  Created by Gregory Fournier on 2018-02-02.
//  Copyright © 2018 Gregory Fournier. All rights reserved.
//

//
//  ViewController.swift
//  dadProject
//
//  Created by Gregory Fournier on 2018-01-28.
//  Copyright © 2018 Gregory Fournier. All rights reserved.
//

import UIKit
import SpreadsheetView
import Firebase

class ViewController: UIViewController, SpreadsheetViewDataSource, SpreadsheetViewDelegate {
    public static var viewController: ViewController?;
    @IBOutlet public weak var spreadsheetView: SpreadsheetView!
    
    let colors = [UIColor(red: 0.314, green: 0.698, blue: 0.337, alpha: 1),
                  UIColor(red: 1.000, green: 0.718, blue: 0.298, alpha: 1),
                  UIColor(red: 0.180, green: 0.671, blue: 0.796, alpha: 1)]
    
    //MARK: Constants
    let Hour_Row_Start: Int = 1;
    let Task_Row_Start: Int = 2;
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        spreadsheetView.dataSource = self
        spreadsheetView.delegate = self
        
        let hairline = 1 / UIScreen.main.scale
        spreadsheetView.intercellSpacing = CGSize(width: hairline, height: hairline)
        spreadsheetView.gridStyle = .solid(width: hairline, color: .lightGray)
        
        spreadsheetView.register(HeaderCell.self, forCellWithReuseIdentifier: String(describing: HeaderCell.self))
        spreadsheetView.register(TextCell.self, forCellWithReuseIdentifier: String(describing: TextCell.self))
        spreadsheetView.register(TaskCell.self, forCellWithReuseIdentifier: String(describing: TaskCell.self))
        spreadsheetView.register(ChartBarCell.self, forCellWithReuseIdentifier: String(describing: ChartBarCell.self))
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        spreadsheetView.flashScrollIndicators()
        // Get tasks online
        ViewController.viewController = self;
        DatabaseManager.initialize();
    }
    
    // MARK: DataSource
    func numberOfColumns(in spreadsheetView: SpreadsheetView) -> Int {
        return 3 + 7 * 8
    }
    
    func numberOfRows(in spreadsheetView: SpreadsheetView) -> Int {
        return 2 + DatabaseManager.urgenceArray.count;
    }
    
    func spreadsheetView(_ spreadsheetView: SpreadsheetView, widthForColumn column: Int) -> CGFloat {
        if case 1...2 = column {
            return 50
        } else {
            return 50
        }
    }
    
    func spreadsheetView(_ spreadsheetView: SpreadsheetView, heightForRow row: Int) -> CGFloat {
        if case 0...1 = row {
            return 28
        } else {
            return 34
        }
    }
    
    func frozenColumns(in spreadsheetView: SpreadsheetView) -> Int {
        return 0
    }
    
    func frozenRows(in spreadsheetView: SpreadsheetView) -> Int {
        return 1
    }
    
    func mergedCells(in spreadsheetView: SpreadsheetView) -> [CellRange] {
        // Days
        var days:[CellRange] = []
        for i in 0...5 {
            days.append(CellRange(from: (0,i*6), to: (0, (i*6) + 5)))
        }
        // Tasks
        let charts = DatabaseManager.urgenceArray.enumerated().map { (index, task) -> CellRange in
            let start = Int(task.arrivalTime)!
            let end = Int(task.duration)
            return CellRange(from: (index + Task_Row_Start, start + 0), to: (index + Task_Row_Start, start + end - 1))
        }
        return  days  + charts
    }
    
    // MARK: What to display in each cell
    func spreadsheetView(_ spreadsheetView: SpreadsheetView, cellForItemAt indexPath: IndexPath) -> Cell? {
        switch (indexPath.column, indexPath.row) { // IMPORTANT (column, row)
        case (0..<(15), 0): // Days
            let cell = spreadsheetView.dequeueReusableCell(withReuseIdentifier: String(describing: HeaderCell.self), for: indexPath) as! HeaderCell
            // Get Todays date
            var date = Date();
            let calendar = Calendar.current;
            date = calendar.date(byAdding: .day, value: indexPath.column / 6, to: date)!;
            let formatter = DateFormatter()
            formatter.timeStyle = .none
            formatter.dateStyle = .medium
            let day = formatter.string(from: date)

            //let text = ("Fév " + String(indexPath.column / 3))
            let text = day.description
            cell.label.text = text;
            cell.gridlines.left = .default
            cell.gridlines.right = .default
            return cell
        case (0..<(7 * 8), 1): // Hours
            let cell = spreadsheetView.dequeueReusableCell(withReuseIdentifier: String(describing: HeaderCell.self), for: indexPath) as! HeaderCell
            let text = ((indexPath.column - 0) % 6) * 4
            cell.label.text = String(text) + "h"
            cell.gridlines.left = .default
            cell.gridlines.right = .default
            return cell
        case (0..<(0 + 7 * 8), Task_Row_Start..<(Task_Row_Start + DatabaseManager.urgenceArray.count)): // Tasks
            let cell = spreadsheetView.dequeueReusableCell(withReuseIdentifier: String(describing: ChartBarCell.self), for: indexPath) as! ChartBarCell
            let start = Int(DatabaseManager.urgenceArray[indexPath.row - Task_Row_Start].arrivalTime)!
            if start == indexPath.column {
                cell.label.text = DatabaseManager.urgenceArray[indexPath.row - Task_Row_Start].operationType
                let colorIndex = indexPath.row % colors.count;
                cell.color = colors[colorIndex]
            } else {
                cell.label.text = ""
                cell.color = .clear
            }
            return cell
        default:
            return nil
        }
    }
    
    /// Delegate
    func spreadsheetView(_ spreadsheetView: SpreadsheetView, didSelectItemAt indexPath: IndexPath) {
        print("Selected: (row: \(indexPath.row), column: \(indexPath.column))")
        //print((spreadsheetView.cellForItem(at: indexPath) as! ChartBarCell).label.text ?? "")
        let urgence: Urgence = DatabaseManager.urgenceArray[indexPath.row - Task_Row_Start];
        print(urgence.toString());
    }
}



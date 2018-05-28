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
    
    var days: [Date] = []; // Header days of the week
    
    //MARK: Constants
    let Hour_Row_Start: Int = 1;
    let Task_Row_Start: Int = 2;
    var DateWidth: Int = 24; // number of cells that each date takes
    var HourZoom: Int = 1; // Granuality (sp??) of hours (4 = 0h,4h,8h,12h...)
    var DaysToShow: Int = 10;
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // create necessary dates
        calculateDates();
        
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
        return DateWidth*DaysToShow;
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
        for i in 0...DaysToShow-1 {
            days.append(CellRange(from: (0,i*DateWidth), to: (0, (i*DateWidth) + (DateWidth-1))))
        }
        // Tasks
        let charts = DatabaseManager.urgenceArray.enumerated().map { (index, task) -> CellRange in
            //let start = task.arrivalTime.getRoundedTime();
            let start = getUrgenceStartColumn(task);
            let end: Int = Int((Int(task.timeToBeginOperation) - Int(task.duration)) / HourZoom)
            return CellRange(from: (index + Task_Row_Start, start + 0), to: (index + Task_Row_Start, start + end - 1))
        }
        return  days  + charts
    }
    
    // MARK: What to display in each cell
    func spreadsheetView(_ spreadsheetView: SpreadsheetView, cellForItemAt indexPath: IndexPath) -> Cell? {
        switch (indexPath.column, indexPath.row) { // IMPORTANT (column, row)
        case (0..<(DateWidth*DaysToShow), 0): // Days
            let cell = spreadsheetView.dequeueReusableCell(withReuseIdentifier: String(describing: HeaderCell.self), for: indexPath) as! HeaderCell
            // Get Todays date
            let formatter = DateFormatter()
            formatter.timeStyle = .none
            formatter.dateStyle = .medium
            let day = formatter.string(from: days[indexPath.column])
            let text = day.description
            cell.label.text = text;
            cell.gridlines.left = .default
            cell.gridlines.right = .default
            return cell
        case (0..<(7 * 8), 1): // Hours
            let cell = spreadsheetView.dequeueReusableCell(withReuseIdentifier: String(describing: HeaderCell.self), for: indexPath) as! HeaderCell
            let text = ((indexPath.column - 0) % DateWidth) * HourZoom
            cell.label.text = String(text) + "h"
            cell.gridlines.left = .default
            cell.gridlines.right = .default
            return cell
        case (0..<(0 + 7 * 8), Task_Row_Start..<(Task_Row_Start + DatabaseManager.urgenceArray.count)): // Tasks
            let cell = spreadsheetView.dequeueReusableCell(withReuseIdentifier: String(describing: ChartBarCell.self), for: indexPath) as! ChartBarCell
            //let start = DatabaseManager.urgenceArray[indexPath.row - Task_Row_Start].arrivalTime.getRoundedTime()
            let start = getUrgenceStartColumn(DatabaseManager.urgenceArray[indexPath.row - Task_Row_Start]);
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
    
    //MARK: Button functions
    @IBAction func onZoomIn(_ sender: UIButton) {
        if (DateWidth >= 24) {
           return;
        }
        DateWidth = DateWidth*2;
        HourZoom = HourZoom/2;
        
        // Recalculate dates
        calculateDates();
        
        // Tell spreadsheet to reload the data on screen
        ViewController.viewController?.spreadsheetView.reloadData();
    }
    
    @IBAction func onZoomOut(_ sender: UIButton) {
        if (DateWidth <= 6) {
            return;
        }
        DateWidth = DateWidth/2;
        HourZoom = HourZoom*2;
        
        // Recalculate dates
        calculateDates();
        
        // Tell spreadsheet to reload the data on screen
        ViewController.viewController?.spreadsheetView.reloadData();
    }
    
    func getUrgenceStartColumn(_ urgence: Urgence) -> Int {
        //var start = urgence.arrivalTime.getRoundedTime();
        var start: Int = urgence.arrivalTime.hour / HourZoom;
        // Décaler start selon la date
        let calendar = NSCalendar.current;
        let date1 = calendar.startOfDay(for: days[0]) // yesterday
        let date2 = calendar.startOfDay(for: urgence.arrivalDate.toDate(calendar as NSCalendar));
        let c = calendar.dateComponents([.day], from: date1, to: date2);
        return start + (c.day! * DateWidth);
    }
    
    func calculateDates() {
        days = [];
        for index in 0..<((DaysToShow+1)*DateWidth) {
            var date = Date();
            let calendar = Calendar.current;
            date = calendar.date(byAdding: .day, value: (index  / DateWidth) - 1, to: date)!;
            days.append(date)
        }
    }
    
    /// Delegate
    func spreadsheetView(_ spreadsheetView: SpreadsheetView, didSelectItemAt indexPath: IndexPath) {
        print("Selected: (row: \(indexPath.row), column: \(indexPath.column))")
        //print((spreadsheetView.cellForItem(at: indexPath) as! ChartBarCell).label.text ?? "")
        let urgence: Urgence = DatabaseManager.urgenceArray[indexPath.row - Task_Row_Start];
        // Show a popup with data
        PopupHandler.Popup("Patient #" + String(urgence.dossier), urgence.toString(), self)
        print(urgence.toString());
    }
}



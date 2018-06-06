package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TablePosition;
import org.controlsfx.control.spreadsheet.*;
import sample.Utils.CellPosition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class GanttChart extends SpreadsheetView {
    private int rowCount = 100;
    private int columnCount = 200;
    private int daysToShow = 3;
    private int dateSpan = 24;
    private int hourSpan = 1;
    private int columnWidth = 40;
    private ArrayList<Urgence> allUrgences = new ArrayList<>();

    Controller controller;

    public Map<CellPosition, Urgence> urgenceCellPositions = new HashMap<>();
    public Urgence selectedUrgence = null;

    public GanttChart() {
        super();
        // init properties
        this.setEditable(false);
        initChart();
        // fix all column widths
        for (int i = 0; i < daysToShow*dateSpan; i++) {
            SpreadsheetColumn column = this.getColumns().get(i);
            column.setMaxWidth(columnWidth);
            column.setMinWidth(columnWidth);
        }
        getGrid().setDisplaySelection(false);
        // Setup database urgence listener
        DatabaseManager.setupUrgenceLoader(this);
        // Setup on selected listener
        this.getSelectionModel().getSelectedCells().addListener(new InvalidationListener() {

            @Override
            public void invalidated(Observable o) {
                TablePosition cell = getSelectionModel().getSelectedCells().get(0);
                CellPosition position = new CellPosition(cell.getRow(), cell.getColumn());
                selectedUrgence = urgenceCellPositions.getOrDefault(position,null);
                System.out.println(selectedUrgence);
                controller.updateDetailsPane(selectedUrgence);
            }
        });

        // Setup context menu
        getContextMenu().getItems().clear();
    }

    private void updateChart() {
        GridBase grid = (GridBase) this.getGrid();
        int row = 2;
        for (Urgence urgence : allUrgences) {
            int column = getStartingColumn(urgence);
            System.out.println("Column: " + column);
            SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1,urgence.getOperationType());
            urgenceCellPositions.put(new CellPosition(row,column), urgence);
            cell.getStyleClass().add("color" + (row%3));
            grid.getRows().get(row).set(column, cell );
            row++;
            this.hideRow(row);
            this.showRow(row);
        }
        for (int i = 0; i < allUrgences.size(); i++) {
            Urgence u = allUrgences.get(i);
            int duration = (u.getTimeToBeginOperation() - u.getDuration());
            getGrid().spanColumn(duration, i+2, getStartingColumn(u) );
        }


    }


    private void initChart() {
        // Hide the headers
        setShowColumnHeader(false);
        urgenceCellPositions.clear();

        GridBase grid = new GridBase(rowCount,columnCount);
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        // First row dates
        final ObservableList<SpreadsheetCell> dates = FXCollections.observableArrayList();
        for (int i = 0; i < daysToShow * dateSpan; i++) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd");
            Date date = new Date();
            date = sample.Utils.DateUtils.addDays(date, (i/dateSpan)-1);
            SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(0, i, 1, 1,dateFormat.format(date));
            cell.getStyleClass().add("dates");
            dates.add(cell);
        }
        rows.add(dates);
        // Row Hours
        final ObservableList<SpreadsheetCell> hours = FXCollections.observableArrayList();
        for (int i = 0; i < daysToShow * dateSpan; i++) {
            String hourValue = String.valueOf(i % 24);
            SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(1, i, 1, 1,hourValue + "h");
            cell.getStyleClass().add("dates");
            hours.add(cell);
        }
        rows.add(hours);
        for (int row = 2; row < daysToShow * dateSpan; row++) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
            for (int column = 0; column < daysToShow * dateSpan; column++) {
                SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1,"");
                list.add(cell);
            }
            rows.add(list);
        }
        grid.setRows(rows);

        for (int i = 0; i < daysToShow * dateSpan; i+=dateSpan) {
            grid.spanColumn(dateSpan,0, i);
        }
        this.setGrid(grid);


    }

    public void addUrgence(Urgence urgence) {
        allUrgences.add(urgence);
        initChart();
        updateChart();
    }

    public void removeUrgence(Urgence urgence) {
        // Get position
        for (CellPosition cellPosition : urgenceCellPositions.keySet()) {
            if (urgenceCellPositions.get(cellPosition).equals(urgence)) {
                // found the position, remove it from grid
                SpreadsheetCell emptyCell = SpreadsheetCellType.STRING.createCell(cellPosition.row, cellPosition.column, 1, 1,"");
                getGrid().getRows().get(cellPosition.row).set(cellPosition.column, emptyCell);
                getGrid().spanColumn(1, cellPosition.row, cellPosition.column);
                urgenceCellPositions.remove(cellPosition);
                // To update Chart
                this.hideRow(cellPosition.row);
                this.showRow(cellPosition.row);
                break;
            }
        }
        allUrgences.remove(urgence);

    }

    private int getStartingColumn(Urgence urgence) {
        // Get lowest possible date, which is yesterday
        DateFormat dateFormat = new SimpleDateFormat("MM/dd");
        Date date = new Date();
        date = sample.Utils.DateUtils.addDays(date, -1);

        int dateMonth = Integer.parseInt(dateFormat.format(date).split("/")[0]);
        int dateDay = Integer.parseInt(dateFormat.format(date).split("/")[1]);
        int columnStart = 0;
        while (!(dateMonth==urgence.getArrivalMonth() && dateDay == urgence.getArrivalDay() )){
            columnStart+= dateSpan;
            date = sample.Utils.DateUtils.addDays(date, 1);
            dateMonth = Integer.parseInt(dateFormat.format(date).split("/")[0]);
            dateDay = Integer.parseInt(dateFormat.format(date).split("/")[1]);
        }
        // Now we have the begining column, we move it based on hour
        columnStart += urgence.getArrivalHour();
        return columnStart;
    }

    public void setController(Controller c) {
        this.controller = c;
    }


}

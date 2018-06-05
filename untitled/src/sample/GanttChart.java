package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TablePosition;
import org.controlsfx.control.spreadsheet.*;

import java.util.ArrayList;

public class GanttChart extends SpreadsheetView {
    private int rowCount = 15;
    private int columnCount = 10;
    private ArrayList<Urgence> allUrgences = new ArrayList<>();

    public GanttChart() {
        super();
        // init properties
        this.setEditable(false);

        initChart();
        // Setup database urgence listener
        DatabaseManager.setupUrgenceLoader(this);
        // Setup on selected listener
        this.getSelectionModel().getSelectedCells().addListener(new InvalidationListener() {

            @Override
            public void invalidated(Observable o) {
                for(TablePosition cell:getSelectionModel().getSelectedCells()){
                    System.out.println(cell.getRow()+"/"+cell.getColumn());
                }
            }
        });
    }

    private void updateChart() {
        Grid grid = this.getGrid();
        int row = 0;
        for (Urgence urgence : allUrgences) {
            int duration = (urgence.getTimeToBeginOperation() - urgence.getDuration());
            Button button = new Button(urgence.getOperationType());
            SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(row, 0, 1, duration,urgence.getOperationType());
            cell.getStyleClass().add("color" + (row%3));
            grid.getRows().get(row).set(0, cell );
            row++;
            this.hideRow(row);
            this.showRow(row);
        }

    }

    private void initChart() {
        // init chart
        GridBase grid = new GridBase(15,15);
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        for (int row = 0; row < 15; row++) {
            final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
            for (int column = 0; column < 15; column++) {
                SpreadsheetCell cell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1,"");
                list.add(cell);
            }
            rows.add(list);
        }
        grid.setRows(rows);
        this.setGrid(grid);
    }

    public void addUrgence(Urgence urgence) {
        allUrgences.add(urgence);
        updateChart();
    }
}

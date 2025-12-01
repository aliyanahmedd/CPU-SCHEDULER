package com.scheduler.view;

import com.scheduler.model.ProcessModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;

/**
 * Custom component for editing process list in a table.
 */
public class ProcessTableView extends BorderPane {
    private TableView<ProcessModel> table;
    private ObservableList<ProcessModel> processes;

    public ProcessTableView() {
        processes = FXCollections.observableArrayList();
        initializeUI();
    }

    private void initializeUI() {
        table = new TableView<>();
        table.setEditable(true);
        table.setItems(processes);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ID column
        TableColumn<ProcessModel, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        idCol.setOnEditCommit(event -> event.getRowValue().setId(event.getNewValue()));
        idCol.setPrefWidth(60);

        // Arrival column
        TableColumn<ProcessModel, Integer> arrivalCol = new TableColumn<>("Arrival");
        arrivalCol.setCellValueFactory(new PropertyValueFactory<>("arrival"));
        arrivalCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        arrivalCol.setOnEditCommit(event -> event.getRowValue().setArrival(event.getNewValue()));
        arrivalCol.setPrefWidth(70);

        // Burst column
        TableColumn<ProcessModel, Integer> burstCol = new TableColumn<>("Burst");
        burstCol.setCellValueFactory(new PropertyValueFactory<>("burst"));
        burstCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        burstCol.setOnEditCommit(event -> event.getRowValue().setBurst(event.getNewValue()));
        burstCol.setPrefWidth(70);

        // Priority column
        TableColumn<ProcessModel, Integer> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        priorityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        priorityCol.setOnEditCommit(event -> event.getRowValue().setPriority(event.getNewValue()));
        priorityCol.setPrefWidth(70);

        table.getColumns().addAll(idCol, arrivalCol, burstCol, priorityCol);
        
        setCenter(table);
    }

    public void addProcess(ProcessModel process) {
        processes.add(process);
    }

    public void removeSelectedProcess() {
        ProcessModel selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            processes.remove(selected);
        }
    }

    public ObservableList<ProcessModel> getProcesses() {
        return processes;
    }

    public void clearProcesses() {
        processes.clear();
    }

    public void setProcesses(ObservableList<ProcessModel> processes) {
        this.processes = processes;
        table.setItems(processes);
    }
}

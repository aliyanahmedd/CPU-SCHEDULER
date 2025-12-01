package com.cpuscheduler.ui;

import com.cpuscheduler.model.Process;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.converter.IntegerStringConverter;

import java.util.List;

/**
 * Process table component for adding, editing, and removing processes.
 */
public class ProcessTable extends VBox {
    private final TableView<Process> table;
    private final ObservableList<Process> processes;
    private int nextProcessId = 1;

    public ProcessTable() {
        this.processes = FXCollections.observableArrayList();
        this.table = createTable();

        Label titleLabel = new Label("Processes");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        HBox controls = createControls();

        this.getChildren().addAll(titleLabel, table, controls);
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        // Add sample processes
        addSampleProcesses();
    }

    @SuppressWarnings("unchecked")
    private TableView<Process> createTable() {
        TableView<Process> tv = new TableView<>();
        tv.setEditable(true);
        tv.setPrefHeight(200);
        tv.setItems(processes);
        tv.setPlaceholder(new Label("No processes. Click 'Add Process' to add one."));

        // ID Column (non-editable)
        TableColumn<Process, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getId()).asObject());
        idCol.setPrefWidth(50);
        idCol.setEditable(false);

        // Name Column
        TableColumn<Process, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getName()));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(e -> e.getRowValue().setName(e.getNewValue()));
        nameCol.setPrefWidth(80);

        // Arrival Time Column
        TableColumn<Process, Integer> arrivalCol = new TableColumn<>("Arrival");
        arrivalCol.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getArrivalTime()).asObject());
        arrivalCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        arrivalCol.setOnEditCommit(e -> {
            if (e.getNewValue() >= 0) {
                e.getRowValue().setArrivalTime(e.getNewValue());
            } else {
                tv.refresh();
            }
        });
        arrivalCol.setPrefWidth(70);

        // Burst Time Column
        TableColumn<Process, Integer> burstCol = new TableColumn<>("Burst");
        burstCol.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getBurstTime()).asObject());
        burstCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        burstCol.setOnEditCommit(e -> {
            if (e.getNewValue() > 0) {
                e.getRowValue().setBurstTime(e.getNewValue());
            } else {
                tv.refresh();
            }
        });
        burstCol.setPrefWidth(70);

        // Priority Column
        TableColumn<Process, Integer> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getPriority()).asObject());
        priorityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        priorityCol.setOnEditCommit(e -> {
            if (e.getNewValue() >= 0) {
                e.getRowValue().setPriority(e.getNewValue());
            } else {
                tv.refresh();
            }
        });
        priorityCol.setPrefWidth(70);

        tv.getColumns().addAll(idCol, nameCol, arrivalCol, burstCol, priorityCol);

        return tv;
    }

    private HBox createControls() {
        Button addBtn = new Button("Add Process");
        addBtn.setOnAction(e -> addProcess());
        
        Button removeBtn = new Button("Remove Selected");
        removeBtn.setOnAction(e -> removeSelectedProcess());
        
        Button clearBtn = new Button("Clear All");
        clearBtn.setOnAction(e -> clearProcesses());

        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.getChildren().addAll(addBtn, removeBtn, clearBtn);
        
        return controls;
    }

    /**
     * Adds a new process with default values.
     */
    public void addProcess() {
        String name = "P" + nextProcessId;
        Process process = new Process(nextProcessId, name, 0, 1, 0);
        processes.add(process);
        nextProcessId++;
        table.scrollTo(process);
        table.getSelectionModel().select(process);
    }

    /**
     * Adds a process with specified values.
     */
    public void addProcess(int arrivalTime, int burstTime, int priority) {
        String name = "P" + nextProcessId;
        Process process = new Process(nextProcessId, name, arrivalTime, burstTime, priority);
        processes.add(process);
        nextProcessId++;
    }

    /**
     * Removes the currently selected process.
     */
    public void removeSelectedProcess() {
        Process selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            processes.remove(selected);
        }
    }

    /**
     * Clears all processes.
     */
    public void clearProcesses() {
        processes.clear();
        nextProcessId = 1;
    }

    /**
     * Returns a copy of the current process list.
     */
    public List<Process> getProcesses() {
        return processes.stream().map(Process::copy).toList();
    }

    /**
     * Sets the process list.
     */
    public void setProcesses(List<Process> newProcesses) {
        clearProcesses();
        for (Process p : newProcesses) {
            processes.add(p);
            if (p.getId() >= nextProcessId) {
                nextProcessId = p.getId() + 1;
            }
        }
    }

    /**
     * Returns the observable list for binding.
     */
    public ObservableList<Process> getObservableProcesses() {
        return processes;
    }

    private void addSampleProcesses() {
        addProcess(0, 5, 2);
        addProcess(1, 3, 1);
        addProcess(2, 8, 3);
        addProcess(3, 2, 4);
    }
}

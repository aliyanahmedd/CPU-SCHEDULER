module com.cpuscheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    
    opens com.cpuscheduler to javafx.fxml;
    opens com.cpuscheduler.model to com.google.gson;
    opens com.cpuscheduler.ui to javafx.fxml;
    opens com.cpuscheduler.util to com.google.gson;
    
    exports com.cpuscheduler;
    exports com.cpuscheduler.model;
    exports com.cpuscheduler.algorithm;
    exports com.cpuscheduler.ui;
    exports com.cpuscheduler.util;
}

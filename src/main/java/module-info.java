module com.nonograms.nonogramsgenerator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.nonograms.nonogramsgenerator to javafx.fxml;
    exports com.nonograms.nonogramsgenerator;
}
module com.nonograms.nonogramsgenerator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.nonograms.nonogramsgenerator to javafx.fxml;
    exports com.nonograms.nonogramsgenerator;
}
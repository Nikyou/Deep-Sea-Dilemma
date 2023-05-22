module com.example.deep_sea_dilemma {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.deep_sea_dilemma to javafx.fxml;
    exports com.deep_sea_dilemma;
    exports com.deep_sea_dilemma.interfaces;
    opens com.deep_sea_dilemma.interfaces to javafx.fxml;
}
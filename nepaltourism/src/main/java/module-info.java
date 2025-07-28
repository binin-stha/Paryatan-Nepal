module com.example.nepaltourism {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.nepaltourism to javafx.fxml;
    exports com.example.nepaltourism;
}
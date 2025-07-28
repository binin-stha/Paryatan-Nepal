package com.example.nepaltourism;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import com.example.nepaltourism.LoginController;
import com.example.nepaltourism.DataManager;


import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DataManager.getInstance().initializeData();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tourist-dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Paryatan Nepal");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

//        LoginController controller = fxmlLoader.getController();
        TouristDashboardController controller = fxmlLoader.getController();
        controller.setPrimaryStage(stage);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

//public class HelloApplication extends Application {
//    @Override
//    public void start(Stage primaryStage) throws IOException {
//        try {
//            // Initialize data manager
//            DataManager.getInstance().initializeData();
//
//            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
//            Scene scene = new Scene(loader.load(), 1200, 800);
//            var cssUrl = getClass().getResource("/css/style.css");
//            if (cssUrl != null) {
//                scene.getStylesheets().add(cssUrl.toExternalForm());
//            } else {
//                System.err.println("CSS file not found: /css/style.css");
//            }
//
//            // Set up primary stage
//            primaryStage.setTitle("Nepal Tourism Management System");
//            primaryStage.setScene(scene);
//            primaryStage.setMinWidth(1000);
//            primaryStage.setMinHeight(700);
//            primaryStage.setMaximized(true);
//
//            // Set reference to primary stage in login controller
//            LoginController controller = loader.getController();
//            controller.setPrimaryStage(primaryStage);
//
//            primaryStage.show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}



//    @Override
//    public void start(Stage primaryStage) {
//        try {
//            // Initialize data manager
//            DataManager.getInstance().initializeData();
//
//            // Load login screen
//            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
//            Scene scene = new Scene(loader.load(), 1200, 800);
//            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
//
//            // Set up primary stage
//            primaryStage.setTitle("Nepal Tourism Management System");
//            primaryStage.setScene(scene);
//            primaryStage.setMinWidth(1000);
//            primaryStage.setMinHeight(700);
//            primaryStage.setMaximized(true);
//
//            // Set reference to primary stage in login controller
//            LoginController controller = loader.getController();
//            controller.setPrimaryStage(primaryStage);
//
//            primaryStage.show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
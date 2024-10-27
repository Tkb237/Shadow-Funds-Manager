package application;

import geldVerwaltung.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import util.other.MonthYear;
import util.other.Prioritaet;
import util.reportGenerator.ReportGenerator;

import java.time.LocalDate;
import java.time.Month;

// App class of SFM

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {

            Konto konto = new Konto("Tkb");

            Pane pane = new MainPane(konto, primaryStage);
            Scene scene = new Scene(pane);
            scene.getStylesheets().add(getClass().getResource("css/light_theme.css").toExternalForm());

            primaryStage.setHeight(800);
            primaryStage.setMinHeight(615);
            primaryStage.setWidth(830);
            primaryStage.setMinWidth(830);
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResource("thief.png").toExternalForm()));
            primaryStage.show();

        } catch (Exception ignored)
        {
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

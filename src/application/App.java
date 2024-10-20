package application;

import geldVerwaltung.Konto;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

// App class of SFM

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {

            Konto konto = new Konto("Tkb");
            /* konto.doTransaktion(new Eingabe("Umzug", 70.0f));
            konto.doTransaktion(new Eingabe("Umzug", 70.0f));
            konto.doTransaktion(new Eingabe("Umzug", 70.0f));
            konto.doTransaktion(new Eingabe("Küche", 700.0f, LocalDate.of(2024, 1, 7)));
            konto.doTransaktion(new Eingabe("Umzug", 900.0f, LocalDate.of(2024, 2, 7)));
            konto.doTransaktion(new Eingabe("Hilfe", 640.0f, LocalDate.of(2024, 3, 7)));
            konto.doTransaktion(new Eingabe("Umzug", 470.0f, LocalDate.of(2024, 5, 7)));
            konto.doTransaktion(new Eingabe("Umzug", 230.0f, LocalDate.of(2024, 9, 7)));


            konto.doTransaktion(new Ausgabe("Essen", 2.5f, Priorität.Superhoch));
            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 8, 7)));

            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 6, 7)));
            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 6, 10)));
            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 8, 7)));
            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 8, 7)));

            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 5, 7)));
            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 4, 10)));
            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 3, 7)));
            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 2, 7)));

            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 1, 7)));
            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 1, 10)));
            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 1, 7)));
            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 1, 7)));

            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 1, 7)));
            konto.doTransaktion(new Ausgabe("Cosmetic", 2000, Priorität.Hoch, LocalDate.of(2024, 12, 10)));
            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 9, 7)));
            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 9, 7)));

            konto.doTransaktion(new Ausgabe("Cosmetic", 30, Priorität.Hoch, LocalDate.of(2024, 7, 7)));
            konto.doTransaktion(new Ausgabe("Cosmetic", 800, Priorität.Hoch, LocalDate.of(2024, 7, 10)));
            konto.doTransaktion(new Ausgabe("Cosmetic", 450, Priorität.Hoch, LocalDate.of(2024, 10, 7)));
            konto.doTransaktion(new Ausgabe("Cosmetic", 700, Priorität.Hoch, LocalDate.of(2024, 11, 7)));


            konto.doTransaktion(new Ausgabe("Essen", 2.5f, Priorität.Superhoch));

            konto.doTransaktion(new Darlehen("Abdel", 1000));
            konto.doTransaktion(new Darlehen("Hassan", 100));

            konto.doTransaktion(new Schuld("Nobody", 1000));


            //Scene scene = new Scene(new Pane());

			
			/*Ziel ziel = new Ziel("My Ziel", Priorität.Niedrig, 30);
			LocalDate date = LocalDate.of(2024, 9, 1);
			ziel.setStartDatum(date);
			ziel.addTask(new Task("Task Special"));

			ziel.addTask(new Task("Task 2"));
			ziel.addTask(new Task("Task 3"));
			ziel.addTask(new Task("Task 4"));
			ziel.addTask(new Task("Task 1"));
			
			Ziel ziel2 = new Ziel("My Ziel2", Priorität.Niedrig, 30);
			
			ziel2.addTask(new Task("Task 1"));
			ziel2.addTask(new Task("Task 2"));
			ziel2.addTask(new Task("Task 3"));
			ziel2.addTask(new Task("Task 4"));
			ziel2.addTask(new Task("Task 1"));
			
			Ziel ziel3 = new Ziel("My Ziel2", Priorität.Niedrig, 30);
			
			ziel3.addTask(new Task("Task 1"));
			ziel3.addTask(new Task("Task 2"));
			ziel3.addTask(new Task("Task 3"));
			ziel3.addTask(new Task("Task 4"));
			ziel3.addTask(new Task("Task 1"));
			
			
			ziel.addTask(ziel2);
			ziel2.addTask(ziel3);


			root.addTask(ziel);
			root.addTask(new Task("Alone"));*/

            //Scene scene = new Scene(root);*/

            Pane pane = new MainPane(konto, primaryStage);
            Scene scene = new Scene(pane);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("light_theme.css").toExternalForm());

            primaryStage.setHeight(800);
            primaryStage.setMinHeight(615);
            primaryStage.setWidth(830);
            primaryStage.setMinWidth(830);
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResource("thief.png").toExternalForm()));
            primaryStage.show();
            System.out.println("ff 2");

        } catch (Exception ignored)
        {
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

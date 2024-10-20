package chartUI;

import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// show graph in another window ("Bigger")
public class GraphShower extends Stage {
    private final StackPane pane = new StackPane();

    public GraphShower(Stage s) {
        Scene scene = new Scene(pane);
        setScene(scene);
        getIcons().add(new Image(getClass().getClassLoader().getResource("icons8-graph-64.png").toExternalForm()));
        setTitle("Graph Shower");
        setOnCloseRequest(e ->
        {
            pane.getChildren().clear();

        });
        pane.setId("mainBoxGS");
        initOwner(s);

    }

    public void changeChart(Chart chart) {
        close(); // we can't open two graph shower
        pane.getChildren().clear();
        pane.getChildren().add(chart);
        show();
    }
}

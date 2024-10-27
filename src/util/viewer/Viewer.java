package util.viewer;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// show Node in another window ("Bigger")
public class Viewer extends Stage {
    private final StackPane pane = new StackPane();

    public Viewer(Stage s) {
        Scene scene = new Scene(pane);
        setScene(scene);
        getIcons().add(new Image(getClass().getClassLoader().getResource("icons8-graph-64.png").toExternalForm()));
        setTitle("Graph Viewer");
        setOnCloseRequest(e -> pane.getChildren().clear());
        pane.setId("mainBoxGS");
        initOwner(s);

    }

    public void setContent(Node node) {
        close(); // we can't open two graph shower
        pane.getChildren().clear();
        pane.getChildren().add(node);
        show();
    }

    public void setContent(Node n, String title, String icon)
    {
        getIcons().clear();
        getIcons().add(new Image(getClass().getClassLoader().getResource(icon).toExternalForm()));
        setTitle(title);
        setContent(n);
    }


}

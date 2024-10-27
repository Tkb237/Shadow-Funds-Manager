package transUI;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class CustomAlert extends Alert {

    public CustomAlert(AlertType at, String style) {
        super(at);
        String pfad = "icons8-confirm-96.png";
        ;
        if (at == AlertType.CONFIRMATION) {
            pfad = "icons8-confirm-96.png";

        } else if (at == AlertType.INFORMATION) {
            pfad = "icons8-info-96.png";

        } else if (at == AlertType.WARNING) {
            pfad = "icons8-warning-96.png";

        } else {
            pfad = "icon.png";

        }
        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getClassLoader().getResource(pfad).toExternalForm()));
        this.getDialogPane().getStylesheets().add(getClass().getResource(TransaktionUi.SRC+style).toExternalForm());
    }

    public void  showTemp()
    {
        initModality(Modality.NONE);
        show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run()
            {
                Platform.runLater(CustomAlert.this::close);
                timer.cancel();
            }
        }, 800);

    }
}

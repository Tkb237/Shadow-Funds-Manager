package application;


import geldVerwaltung.Konto;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import transUI.CustomAlert;

// The window is used to create a new account
public class CreateAccountUI extends Stage {

    private String ss = "light.css";
    private StackPane mainBox = new StackPane();
    private GridPane pane = new GridPane();
    //
    private Label schuld = new Label("Schuld: ");
    private Label darlehen = new Label("Darlehen: ");
    private Label saldo = new Label("Saldo: ");
    private Label inhaber = new Label("Inhaber: ");

    //
    private TextField schuldField = new TextField();
    private TextField darlehenField = new TextField();
    private TextField saldoField = new TextField();
    private TextField inhaberField = new TextField();
    //
    HBox btnBox = new HBox();
    private Button validBtn = new Button("Valid");
    private Button abortBtn = new Button("Abort");
    private Scene scene;
    //
    private Konto konto;


    public CreateAccountUI(Stage s) {
        showUi();
        validBtn();
        setTitle("New Account");
        initModality(Modality.APPLICATION_MODAL);
        initOwner(s);
        getIcons().add(new Image(getClass().getClassLoader().getResource("icons8-registration-96.png").toExternalForm()));
        setMinWidth(255);
        setMaxWidth(255);

        setMaxHeight(310);
        setMinHeight(310);
    }

    public void custom(String file) {
        scene.getStylesheets().add(getClass().getResource("css/"+file).toExternalForm());
        ss = file.contains("dark") ? "dark.css" : "light.css";
    }


    private void showUi() {
        CheckBox activeBox =  new CheckBox("Change");
        activeBox.setSelected(true);

        darlehenField.setText("0");
        saldoField.setText("0");
        schuldField.setText("0");

        darlehenField.disableProperty().bind(activeBox.selectedProperty());
        saldoField.disableProperty().bind(activeBox.selectedProperty());
        schuldField.disableProperty().bind(activeBox.selectedProperty());

        btnBox.getChildren().addAll(validBtn, abortBtn);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setSpacing(9);

        pane.add(darlehen, 0, 1);
        pane.add(darlehenField, 1, 1);

        pane.add(saldo, 0, 2);
        pane.add(saldoField, 1, 2);

        pane.add(schuld, 0, 3);
        pane.add(schuldField, 1, 3);

        pane.add(inhaber, 0, 0);
        pane.add(inhaberField, 1, 0);

        pane.add(activeBox, 1, 5);

        pane.add(btnBox, 1, 6);

        pane.setHgap(12);
        pane.setVgap(12);

        pane.setPadding(new Insets(8));

        scene = new Scene(mainBox);
        mainBox.getChildren().addAll(pane);
        mainBox.setId("mainBox");
        setScene(scene);
        abortBtn();
    }

    private boolean checkedInput() {
        // check if all the fields aren't empty and if the amount field contains only number
        if (checkedField(darlehenField) || checkedField(inhaberField) || checkedField(schuldField) || checkedField(saldoField)) {
            CustomAlert alert = new CustomAlert(AlertType.WARNING, ss);
            alert.setTitle("Fehlende Information");
            alert.setContentText("Das Feld Bezeichnung || Betrag darf nicht leer sein!!! Füllen Sie das Feld aus.");
            alert.showAndWait();
            return false;
        }

        if (checkedInput(darlehenField) && checkedInput(saldoField) && checkedInput(schuldField)) {
            return true;
        } else {
            Alert alert = new CustomAlert(AlertType.WARNING, ss);
            alert.setTitle("Decimal Number missing");
            alert.setContentText("Bitte Geben Sie einen gültige Betrage ein !!!");
            alert.showAndWait();
            return false;
        }
    }

    private boolean checkedField(TextField tf) {
        String inhalt = tf.getText();
        return (inhalt.isBlank() || inhalt.isEmpty());
    }

    private boolean checkedInput(TextField tf) {
        String be = tf.getText();
        return be.matches("[0-9]{1,8}(\\.[0-9]{0,2})?");
    }

    private void validBtn() {
        validBtn.setOnAction(e ->
        {
            if (checkedInput()) {
                String inhaber = inhaberField.getText();
                float saldo = Float.parseFloat(saldoField.getText());
                float darlehen = Float.parseFloat(darlehenField.getText());
                float schuld = Float.parseFloat(schuldField.getText());
                konto = new Konto(saldo, schuld, darlehen, inhaber);
                CustomAlert alert = new CustomAlert(AlertType.INFORMATION, ss);
                alert.setTitle("Erfolg");
                alert.setContentText("Erfolgreich hinzugefügt");
                alert.showAndWait();
                close();
            }
        });
    }

    private void abortBtn() {
        abortBtn.setOnAction(e -> close());
    }

    public Konto getKonto() {
        return konto;
    }

}

		



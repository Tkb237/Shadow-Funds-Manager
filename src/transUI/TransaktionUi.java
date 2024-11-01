package transUI;

import java.time.LocalDate;

import geldVerwaltung.Ausgabe;
import geldVerwaltung.Darlehen;
import geldVerwaltung.Eingabe;
import geldVerwaltung.Konto;
import util.cusWidget.CustomAlert;
import util.other.Prioritaet;
import geldVerwaltung.Schuld;
import geldVerwaltung.Transaktion;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TransaktionUi extends Stage {
    public static final String SRC = "/style/";
    private final StackPane mainBox = new StackPane();
    private final GridPane pane = new GridPane();
    //
    private final Label bezeichnung = new Label("Quelle: ");
    private final Label betrag = new Label("Betrag: ");
    private final Label date = new Label("Datum: ");
    private final Label beschreibung = new Label("Beschreibung: ");
    private final Label kategorie = new Label("Kategorie: ");
    private final Label prioritaet = new Label("Priorität: ");
    //
    private final TextField bezeichnungField = new TextField();
    private final TextField betragField = new TextField();
    private final DatePicker dateField = new DatePicker(LocalDate.now());
    private final TextArea beschreibungField = new TextArea();
    private final ChoiceBox<String> kategorieField = new ChoiceBox<String>(FXCollections.observableArrayList("Eingabe", "Ausgabe", "Darlehen", "Schuld"));
    private final ChoiceBox<Prioritaet> prioritaetFeld = new ChoiceBox<Prioritaet>(FXCollections.observableArrayList(Prioritaet.Superhoch, Prioritaet.Hoch, Prioritaet.Normal, Prioritaet.Niedrig));
    //
    HBox btnBox = new HBox();
    private final Button validBtn = new Button("Valid");
    private final Button updateBtn = new Button("Update");
    private final Button abortBtn = new Button("Abort");
    private Scene scene;

    public TransaktionUi(Stage s, Konto k, Transaktion transaktion) {
        showUi();
        loadTransactionUI(transaktion);
        onActionUpdate(transaktion, k);
        setTitle("Update");
        initModality(Modality.APPLICATION_MODAL);
        initOwner(s);
        //getIcons().add(new Image("file:./src/icons/icons8-money-circulation-96.png"));
        getIcons().add(new Image(getClass().getResource("/icons/icons8-money-circulation-96.png").toExternalForm()));
        setFixedSize();
    }

    public TransaktionUi(Stage s, Konto k) {
        showUi();
        onActionAdd(k);
        setTitle("Hinzufügen");
        initModality(Modality.APPLICATION_MODAL);
        initOwner(s);
        getIcons().add(new Image(getClass().getResource("/icons/icons8-money-circulation-96.png").toExternalForm()));
        setFixedSize();
    }


   private void setFixedSize()
    {
        setMaxHeight((float) 488.0);
        setMinHeight((float) 488.0);
        setMinWidth((float) 610.0);
        setMaxWidth((float) 610.0);
    }

    public void custom(String file) {
        scene.getStylesheets().add(getClass().getResource(SRC+file).toExternalForm());
    }

    private void loadTransactionUI(Transaktion t) {
        btnBox.getChildren().set(0, updateBtn);
        bezeichnungField.setText(t.getBezeichnung());
        beschreibungField.setText(t.getBeschreibung());
        betragField.setText(String.valueOf(Math.abs(t.getBetrag())));
        dateField.setValue(t.getDate());
        kategorieField.getSelectionModel().select(t.getKategorie());
        kategorieField.setDisable(true);
    }

    private void showUi() {
        prioritaetFeld.getSelectionModel().select(2);
        kategorieField.getSelectionModel().selectFirst();
        prioritaetFeld.setDisable(true);

        beschreibung.setMinWidth(30);

        btnBox.getChildren().addAll(validBtn, abortBtn);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setSpacing(9);

        pane.add(kategorie, 0, 1);
        pane.add(kategorieField, 1, 1);

        pane.add(betrag, 0, 2);
        pane.add(betragField, 1, 2);

        pane.add(date, 0, 3);
        pane.add(dateField, 1, 3);

        pane.add(bezeichnung, 0, 0);
        pane.add(bezeichnungField, 1, 0);

        pane.add(prioritaet, 0, 4);
        pane.add(prioritaetFeld, 1, 4);

        pane.add(beschreibung, 0, 5);
        pane.add(beschreibungField, 1, 5);

        pane.add(btnBox, 1, 7);

        pane.setHgap(12);
        pane.setVgap(12);

        pane.setPadding(new Insets(8));

        scene = new Scene(mainBox);
        mainBox.getChildren().addAll(pane);
        mainBox.setId("mainBox");
        setScene(scene);
    }

    private boolean checkedInput(String bez, String be) {
        if (bez.isBlank() || bez.isEmpty() || be.isBlank() || be.isEmpty()) {
            CustomAlert alert = new CustomAlert(AlertType.WARNING);
            alert.setTitle("Fehlende Information");
            alert.setContentText("Das Feld Bezeichnung || Betrag darf nicht leer sein!!! Füllen Sie das Feld aus.");
            alert.showAndWait();
            return false;
        }
        if (!be.matches("[0-9]{1,8}(\\.[0-9]{0,2})?")) {
            Alert alert = new CustomAlert(AlertType.WARNING);
            alert.setTitle("Decimal Number missing");
            alert.setContentText("Bitte Geben Sie einen gültigen Betrag ein !!!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void validBtn(Konto k) {
        validBtn.setOnAction(e ->
        {
            String bez = bezeichnungField.getText();
            String be = betragField.getText();
            String kat = kategorieField.getSelectionModel().getSelectedItem();
            if (checkedInput(bez, be)) {
                if (kat.equals("Eingabe")) {
                    k.doTransaktion(new Eingabe(bez, Float.parseFloat(be), dateField.getValue()));
                } else if (kat.equals("Ausgabe")) {
                    k.doTransaktion(new Ausgabe(bez, Float.parseFloat(be), prioritaetFeld.getSelectionModel().getSelectedItem(), dateField.getValue()));
                } else if (kat.equals("Darlehen")) {
                    if (dateField.getValue().equals(LocalDate.now())) {
                        k.doTransaktion(new Darlehen(bez, Float.parseFloat(be), dateField.getValue().plusDays(30)));
                    } else {
                        k.doTransaktion(new Darlehen(bez, Float.parseFloat(be), dateField.getValue()));
                    }

                } else {
                    if (dateField.getValue().equals(LocalDate.now())) {
                        k.doTransaktion(new Schuld(bez, Float.parseFloat(be), dateField.getValue().plusDays(30)));
                    } else {
                        k.doTransaktion(new Schuld(bez, Float.parseFloat(be), dateField.getValue()));
                    }
                }

                if (!beschreibungField.getText().isBlank() && !beschreibungField.getText().isEmpty())
                    k.getTransaktions().get(k.getTransaktions().size() - 1).setBeschreibung(beschreibungField.getText());

                CustomAlert alert = new CustomAlert(AlertType.INFORMATION);
                alert.setTitle("Erfolg");
                alert.setContentText("Erfolgreich hinzugefügt");
                alert.showTemp();

            }
        });
    }



    private void abortBtn() {
        abortBtn.setOnAction(e -> close());
    }

    private void updateBtn(Transaktion t, Konto k) {
        updateBtn.setOnAction(e ->
        {
            String bez = bezeichnungField.getText();
            String be = betragField.getText();
            if (checkedInput(bez, be)) {
                k.updateTransaktion(t, bez, beschreibungField.getText(), be, dateField.getValue(), prioritaetFeld.getValue());
                CustomAlert alert = new CustomAlert(AlertType.INFORMATION);
                alert.setTitle("Erfolg");
                alert.setContentText("Erfolgreich updated");
                alert.showAndWait();
                close();
            }
        });
    }

    private void onActionDefault() {
        if (kategorieField.getSelectionModel().selectedItemProperty().get().equals("Ausgabe")) {
            prioritaetFeld.setDisable(false);
            bezeichnung.setText("Wozu ?: ");
        }
        kategorieField.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            // different values of the kategorie field
            @Override
            public void changed(ObservableValue<? extends String> arg0, String oldS, String newS) {
                if (newS.equals("Ausgabe")) {
                    prioritaetFeld.setDisable(false);
                    bezeichnung.setText("Wozu ?: ");
                } else prioritaetFeld.setDisable(true);

                if (newS.equals("Eingabe")) {
                    bezeichnung.setText("Quelle: ");
                } else if (newS.equals("Schuld") || newS.equals("Darlehen")) {
                    bezeichnung.setText("Wer ?: ");

                }
            }
        });

        abortBtn();
    }

    private void onActionUpdate(Transaktion t, Konto k) {
        onActionDefault();
        updateBtn(t, k);
    }

    private void onActionAdd(Konto k) {
        onActionDefault();
        validBtn(k);
    }
}

	

package transUI;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import application.MainPane;
import event.CusEventTable;
import event.StateChangedEvent;
import geldVerwaltung.Eingabe;
import geldVerwaltung.Konto;
import util.cusWidget.CustomAlert;
import util.other.Prioritaet;
import geldVerwaltung.Transaktion;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static util.other.Template.df;

public class TransaktionStack extends StackPane {
    private String pfadCss = "light_theme.css";
    private final TableView<Transaktion> mainTab = new TableView<>();

    private Konto myKonto;

    private final Button addButton = new Button("Do Transaction");
    private final Button removeButton = new Button("Undo Transaction");
    private final Button updateButton = new Button("Update Transaction");
    // Radio buttons
    private final RadioButton schuldButton = new RadioButton("Schuld");
    private final RadioButton darlehenButton = new RadioButton("Darlehen");
    private final RadioButton eingabeButton = new RadioButton("Eingabe");
    private final RadioButton ausgabeButton = new RadioButton("Ausgabe");

    private final RadioButton allButton = new RadioButton("All");
    protected boolean emitted = false;

    public TransaktionStack(Stage s, Konto k) {
        this.myKonto = k;
        FXCollections.observableArrayList(myKonto.getTransaktions());
        show();
        setUp(s);
        signalChanges();
    }

    private void setUp(Stage s) {
        //
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //
        getChildren().add(mainTab);
        setAlignment(Pos.CENTER);
        //
        onActionButton(s);
        mainTab.setMaxWidth(Double.MAX_VALUE);
        mainTab.setMaxHeight(Double.MAX_VALUE);
        addToggleButton();
    }

    public ObservableList<Button> addButtons() {
        addButton.setMaxWidth(Double.MAX_VALUE);
        removeButton.setMaxWidth(Double.MAX_VALUE);
        updateButton.setMaxWidth(Double.MAX_VALUE);
        return FXCollections.observableArrayList(addButton, removeButton, updateButton);
    }

    private void addToggleButton() {
        //
        ToggleGroup radioGroup = new ToggleGroup();


        allButton.setSelected(true);
        showAll(myKonto.getTransaktions());

        //
        radioGroup.getToggles().addAll(schuldButton, darlehenButton, eingabeButton, ausgabeButton, allButton);
        schuldButton.setOnAction(e -> showDarlehenSchuld(myKonto.getSchuldenList()));
        darlehenButton.setOnAction(e -> showDarlehenSchuld(myKonto.getDarlehenList()));
        eingabeButton.setOnAction(e -> showEingabe(myKonto.getEingabelist()));
        ausgabeButton.setOnAction(e -> showAusgabe(myKonto.getAusgabelist()));
        allButton.setOnAction(e -> showAll(myKonto.getTransaktions()));

    }

    private String getChoice() {
        try {
            String[] IDs = (myKonto.getTransaktions().stream().map(Transaktion::getiD).toArray(String[]::new));
            /* ****************************************************************************************/
            ChoiceDialog<String> diag = new ChoiceDialog<String>(IDs[0], IDs);
            String iconPfad = getClass().getResource("/icons/icons8-select-64.png").toExternalForm();
            Stage stage = (Stage) diag.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(iconPfad));
            diag.getDialogPane().getStylesheets().add(getClass().getResource
                    (TransaktionUi.SRC + MainPane.getCssForCusAlert()).toExternalForm());
            /* ****************************************************************************************/
            Optional<String> result = diag.showAndWait();
            return result.orElse("1");
        } catch (Exception e) {
            return "1";
        }


    }

    private String getPfadCssFor()
    {
        return pfadCss.contains("dark") ? "cusAlertDark.css" : "cusAlertLight.css";

    }

    private void onActionButton(Stage s) {
        // show the second window
        addButton.setOnAction(e ->
                {
                    int before = myKonto.getTransaktions().size();
                    showSecond(s, myKonto);
                    if (before < myKonto.getTransaktions().size()) {
                        fireEvent(new CusEventTable(CusEventTable.ADD_EVENT_TYPE));
                    }
                    // we should only fire the event if an element was added
                }

        );

        // remove a transaction
        removeButton.setOnAction(e ->
        {
            String erg = getChoice();
            if (!erg.equals("1")) {
                Transaktion t = myKonto.getTransaktions().stream().filter(trans -> trans.getiD().equals(erg)).findFirst().get();
                myKonto.unDoTransaktion(t);
                // UI aktualisieren
                showAll(myKonto.getTransaktions());
                allButton.setSelected(true);
                fireEvent(new CusEventTable(CusEventTable.REMOVE_EVENT_TYPE));
            }
            if (myKonto.getTransaktions().isEmpty()) {
                CustomAlert alert = new CustomAlert(AlertType.INFORMATION);
                alert.setContentText("Keine Transaktion vorhanden");
                alert.show();
            }


        });

        updateButton.setOnAction(e ->
        {
            String erg = getChoice();
            if (!erg.equals("1")) {
                Transaktion t = myKonto.getTransaktions().stream().filter(trans -> trans.getiD().equals(erg)).findFirst().get();
                TransaktionUi myUi = new TransaktionUi(s, myKonto, t);
                myUi.custom(pfadCss);
                myUi.showAndWait();
                // UI aktualisieren
                showAll(myKonto.getTransaktions());
                allButton.setSelected(true);
                fireEvent(new CusEventTable(CusEventTable.UPDATE_EVENT_TYPE));
            }
            if (myKonto.getTransaktions().isEmpty()) {
                CustomAlert alert = new CustomAlert(AlertType.INFORMATION);
                alert.setContentText("Keine Transaktion vorhanden");
                alert.show();
            }

        });

    }

    private void showSecond(Stage s, Konto k) {
        TransaktionUi myUi = new TransaktionUi(s, k);
        myUi.custom(pfadCss);
        myUi.showAndWait();
    }

    @SuppressWarnings("unchecked")
    public void show() {
        // Spalte erstellen
        TableColumn<Transaktion, String> idColumn = new TableColumn<Transaktion, String>("ID");
        TableColumn<Transaktion, String> bezeichnungColumn = new TableColumn<Transaktion, String>("Bezeichnung");
        TableColumn<Transaktion, String> kategorieColumn = new TableColumn<Transaktion, String>("Kategorie");
        TableColumn<Transaktion, String> betragColumn = new TableColumn<Transaktion, String>("Betrag");
        TableColumn<Transaktion, LocalDate> dateColumn = new TableColumn<Transaktion, LocalDate>("Datum");
        TableColumn<Transaktion, String> beschreibungColumn = new TableColumn<Transaktion, String>("Beschreibung");
        //
        idColumn.setMaxWidth(200);
        idColumn.setMinWidth(45);
        // Spalte mit einem Attribut verbinden
        beschreibungColumn.setCellValueFactory(s -> new SimpleStringProperty(s.getValue().getBeschreibung()));
        bezeichnungColumn.setCellValueFactory(s -> new SimpleStringProperty(s.getValue().getBezeichnung()));
        kategorieColumn.setCellValueFactory(s -> new SimpleStringProperty(s.getValue().getKategorie()));
        betragColumn.setCellValueFactory(s -> new SimpleStringProperty(df.format(s.getValue().getBetrag()).
                replace(".", ",") + " $"));
        dateColumn.setCellValueFactory(s -> new SimpleObjectProperty<LocalDate>(s.getValue().getDate()));
        idColumn.setCellValueFactory(s -> new SimpleStringProperty(s.getValue().getiD()));

        // sort id column
        idColumn.setComparator(Comparator.comparingInt(a -> Integer.parseInt(a.replace("Transaktion Nr: ", ""))));
        betragColumn.setComparator(Comparator.comparing(a -> Float.parseFloat(a.replace(",", ".").replace("$", ""))));

        //Spalten anfügen
        mainTab.getColumns().addAll(idColumn, bezeichnungColumn, kategorieColumn, betragColumn, dateColumn, beschreibungColumn);
        mainTab.getColumns().forEach(col -> col.setMinWidth(80));
        mainTab.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);        //Entferne die leere Spalte

    }

    private void signalChanges() {
        // fire an event if a change occurs in the list
        mainTab.getItems().addListener(new ListChangeListener<Transaktion>() {

            @Override
            public void onChanged(Change<? extends Transaktion> changed) {
                while (changed.next()) {
                    if ((changed.wasAdded() || changed.wasUpdated() || changed.wasRemoved()) && !emitted) {
                        fireEvent(new StateChangedEvent(StateChangedEvent.CHANGED_EVENT_TYPE));
                        emitted = true;
                    }
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void showAusgabe() {
        showEingabe();
        ((TableColumn<Transaktion, String>) mainTab.getColumns().toArray()[1]).setText("Wozu ?");
        ((TableColumn<Transaktion, String>) mainTab.getColumns().toArray()[4]).setText("Datum");
        TableColumn<Transaktion, Prioritaet> prioColumn = new TableColumn<Transaktion, Prioritaet>("Priorität");

        mainTab.getColumns().add(prioColumn);

        prioColumn.setMinWidth(80);

    }

    @SuppressWarnings("unchecked")
    private void showEingabe() {
        if (mainTab.getColumns().size() == 7) {
            mainTab.getColumns().remove(6);
        }

        mainTab.getItems().clear();
        ((TableColumn<Transaktion, String>) mainTab.getColumns().toArray()[1]).setText("Quelle");
        ((TableColumn<Transaktion, String>) mainTab.getColumns().toArray()[4]).setText("Datum");
    }

    @SuppressWarnings({"unchecked"})
    public void showDarlehenSchuld(List<? extends Transaktion> list) {
        showAll(list);
        ((TableColumn<Transaktion, String>) mainTab.getColumns().toArray()[1]).setText("Wer ?");
        ((TableColumn<Transaktion, LocalDate>) mainTab.getColumns().toArray()[4]).setText("Rückgabedatum");


    }

    public void showAll(List<? extends Transaktion> list) {
        if (mainTab.getColumns().size() == 7) {
            mainTab.getColumns().remove(6);
        }

        if (mainTab.getColumns().toArray()[4] instanceof TableColumn<?, ?> t) t.setText("Datum");
        mainTab.getItems().clear();
        list.forEach(s -> mainTab.getItems().add(s));
    }

    public void showEingabe(List<Eingabe> list) {
        showEingabe();
        list.forEach(s -> mainTab.getItems().add(s));
    }

    public void showAusgabe(List<Eingabe> list) {
        showAusgabe();
        list.forEach(s -> mainTab.getItems().add(s));
    }

    public ObservableList<RadioButton> getRadioButtons() {
        return FXCollections.observableArrayList(allButton, eingabeButton, ausgabeButton, darlehenButton, schuldButton);
    }

    public void setMyKonto(Konto konto) {
        this.myKonto = konto;
    }

    public void setStyleTrUI(String file) {
        this.pfadCss = file;
    }

    public void setEmitted(boolean emitted) {
        this.emitted = emitted;
    }
}

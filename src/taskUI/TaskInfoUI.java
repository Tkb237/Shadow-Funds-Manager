package taskUI;

import java.time.LocalDate;

import geldVerwaltung.Prioritaet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import taskVerwaltung.Task;
import taskVerwaltung.Ziel;
import transUI.CustomAlert;

public class TaskInfoUI extends StackPane {
    private GridPane pane = new GridPane();
    //
    private Label titel = new Label("");
    private Label bezeichnung = new Label("Bezeichnung: ");
    private Label zeit = new Label("Verbleibende Zeit: ");
    private Label startDate = new Label("Startdatum: ");
    private Label EndDate = new Label("Enddatum: ");

    private Label progress = new Label("Progress: ");

    private Label beschreibung = new Label("Beschreibung: ");
    private Label prioritaet = new Label("Priorität: ");
    //
    private TextField bezeichnungField = new TextField();
    private TextField zeitField = new TextField();
    private DatePicker dateField = new DatePicker(LocalDate.now());
    private DatePicker dateFieldEndDate = new DatePicker(LocalDate.now());
    private TextArea beschreibungField = new TextArea();
    private ProgressBar progressBarZeit = new ProgressBar();
    private ProgressIndicator pi = new ProgressIndicator();
    private ChoiceBox<Prioritaet> prioritaetFeld = new ChoiceBox<Prioritaet>(FXCollections
            .observableArrayList(Prioritaet.Superhoch, Prioritaet.Hoch, Prioritaet.Normal, Prioritaet.Niedrig));
    //
    HBox btnBox = new HBox();
    private Button updateBtn = new Button("Update");
    private String pfadCss = "light_theme.css";

    public TaskInfoUI(TreeView<Task> tree) {
        showUi();
        updateBtn.setOnAction(e -> updateOnAction(tree));

    }

    private void updateOnAction(TreeView<Task> tree) {
        CheckBoxTreeItem<Task> taskItem = (CheckBoxTreeItem<Task>) tree.getSelectionModel().selectedItemProperty()
                .get();
        if (taskItem != null) {
            if (bezeichnungField.getText() == null | bezeichnungField.getText().isBlank()
                    | bezeichnungField.getText().isEmpty()) {
                CustomAlert alert = new CustomAlert(AlertType.INFORMATION, pfadCss.replace("_theme", ""));
                alert.setContentText("Die Bezeichnung darf nicht leer sein");
                alert.showAndWait();
            } else if (dateField.getValue().isAfter(dateFieldEndDate.getValue())) {
                CustomAlert alert = new CustomAlert(AlertType.INFORMATION, pfadCss.replace("_theme", ""));
                alert.setContentText("Das Enddatum muss größer als das Startdatum sein");
                alert.showAndWait();
            } else {
                Task t = taskItem.getValue();
                t.setAbschlussDatum(dateFieldEndDate.getValue());
                t.setStartDatum(dateField.getValue());
                t.setBeschreibung(beschreibungField.getText());
                t.setBezeichnung(bezeichnungField.getText());
                t.setPrio(prioritaetFeld.getValue());
                taskItem.setValue(t);
                tree.refresh();
                getData(t, taskItem);


            }
        }
    }

    private void someListeners() {

        DatePicker[] lists = {dateField, dateFieldEndDate};
        // Minimal date 1.1.1999
        for(DatePicker datePicker: lists)
        {
            datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
                @Override
                public void changed(ObservableValue<? extends LocalDate> arg0, LocalDate old, LocalDate newV) {
                    if (newV != null && newV.isAfter(LocalDate.of(1999, 1, 1)))
                        dateField.setValue(LocalDate.of(1999, 1, 1));
                }
            });
        }
        // limit the number of character
        beschreibungField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String old, String newVal) {
                if (newVal != null && newVal.length() > 50) {
                    beschreibungField.setText(old);
                }
            }
        });
        //
        bezeichnungField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String old, String newVal) {
                if (newVal != null && newVal.length() > 20) {
                    bezeichnungField.setText(old);
                }
            }
        });

    }

    private void showUi() {
        titel.setFont(new Font("Lucida Console", 15));
        titel.setTextAlignment(TextAlignment.CENTER);
        titel.setText("Task || Task Nr: ???");
        titel.setPrefWidth(Double.MAX_VALUE);
        someListeners();
        bezeichnung.setMinWidth(100);
        zeit.setMinWidth(100);
        startDate.setMinWidth(100);
        EndDate.setMinWidth(100);
        progress.setMinWidth(100);
        beschreibung.setMinWidth(100);
        prioritaet.setMinWidth(100);

        zeitField.setDisable(true);

        HBox progressBox = new HBox();
        progressBox.setSpacing(3);
        progressBox.getChildren().addAll(progressBarZeit, pi);
        pi.setMinHeight(30);
        pi.setMinWidth(30);
        pi.setProgress(0);
        progressBarZeit.setPrefWidth(600);
        prioritaetFeld.getSelectionModel().select(2);

        btnBox.getChildren().addAll(updateBtn);
        btnBox.setAlignment(Pos.CENTER);

        pane.add(titel, 0, 0, 3, 1);

        pane.add(zeit, 0, 2);
        pane.add(zeitField, 1, 2);

        pane.add(progress, 0, 3);
        pane.add(progressBox, 1, 3);

        pane.add(startDate, 0, 4);
        pane.add(dateField, 1, 4);

        pane.add(EndDate, 0, 5);
        pane.add(dateFieldEndDate, 1, 5);

        pane.add(bezeichnung, 0, 1);
        pane.add(bezeichnungField, 1, 1);

        pane.add(prioritaet, 0, 6);
        pane.add(prioritaetFeld, 1, 6);

        pane.add(beschreibung, 0, 7);
        pane.add(beschreibungField, 1, 7);

        pane.add(btnBox, 1, 9);

        pane.setHgap(12);
        pane.setVgap(12);

        pane.setPadding(new Insets(8));

        this.getChildren().add(pane);

    }

    public void getData(Task task, CheckBoxTreeItem<Task> taskItem) {
        if (task instanceof Ziel) {
            String.valueOf(((Ziel) task).getSize());
        }
        titel.setText(task.getBezeichnung() + " || " + task.getIdString());
        zeitField.setText(String.valueOf(task.getVerbleibendeZeit()) + " Tage");
        prioritaetFeld.setValue(task.getPrio());
        bezeichnungField.setText(task.getBezeichnung());
        beschreibungField.setText(task.getBeschreibung());
        dateField.setValue(task.getStartDatum());
        dateFieldEndDate.setValue(task.getAbschlussDatum());
        updateProgressBar(task, taskItem);
    }

    public boolean updateProgressBar(Task task, CheckBoxTreeItem<Task> taskItem) {
        // double progress = (double) (task.getPastDays()) / task.getDauer();
        double progress = CustomTreeCell.getAnzahlCheckedTasks(taskItem) / Ziel.getAnzahlSubTAsks(task);
        progressBarZeit.setProgress(progress);
        pi.setProgress(progress);
        return Double.compare(progress, 1.0) >= 0 ? true : false;
    }

    public void updateBezeichnung(String t) {
        bezeichnungField.setText(t);
    }

    public void setPfadCss(String pfadCss) {
        this.pfadCss = pfadCss;
    }


}

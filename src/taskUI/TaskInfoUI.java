package taskUI;

import java.time.LocalDate;

import javafx.scene.Node;
import javafx.scene.control.*;
import util.other.Prioritaet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import taskVerwaltung.Task;
import taskVerwaltung.Ziel;
import util.cusWidget.CustomAlert;

public class TaskInfoUI extends StackPane {
    private final GridPane pane = new GridPane();
    //
    private final Label titel = new Label("");
    private final Label bezeichnung = new Label("Bezeichnung: ");
    private final Label zeit = new Label("Restzeit: ");
    private final Label startDate = new Label("Startdatum: ");
    private final Label EndDate = new Label("Enddatum: ");

    private final Label progress = new Label("Progress: ");

    private final Label beschreibung = new Label("Beschreibung: ");
    private final Label prioritaet = new Label("Priorität: ");
    //
    private final TextField bezeichnungField = new TextField();
    private final TextField zeitField = new TextField();
    private final DatePicker dateField = new DatePicker(LocalDate.now());
    private final DatePicker dateFieldEndDate = new DatePicker(LocalDate.now());
    private final TextArea beschreibungField = new TextArea();
    private final ProgressBar progressBarZeit = new ProgressBar();
    private final ProgressIndicator pi = new ProgressIndicator();
    private final ChoiceBox<Prioritaet> prioritaetFeld = new ChoiceBox<Prioritaet>(FXCollections
            .observableArrayList(Prioritaet.Superhoch, Prioritaet.Hoch, Prioritaet.Normal, Prioritaet.Niedrig));
    //
    private final Button updateBtn = new Button("Update");
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
                CustomAlert alert = new CustomAlert(AlertType.INFORMATION);
                alert.setContentText("Die Bezeichnung darf nicht leer sein");
                alert.showAndWait();
            } else if (dateField.getValue().isAfter(dateFieldEndDate.getValue())) {
                CustomAlert alert = new CustomAlert(AlertType.INFORMATION);
                alert.setContentText("Das Enddatum muss nach dem Startdatum liegen.");
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
                setData(t, taskItem);
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
                    if (newV != null && newV.isBefore(LocalDate.of(1999, 1, 1)))
                        datePicker.setValue(LocalDate.of(1999, 1, 1));
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
        titel.setText("Task");
        titel.setPrefWidth(Double.MAX_VALUE);
        titel.setId("taskTitle");
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

        updateBtn.setAlignment(Pos.CENTER);
        updateBtn.setMaxWidth(Double.MAX_VALUE);
        //HBox.setHgrow(b);

        titel.setTextAlignment(TextAlignment.CENTER);
        titel.setAlignment(Pos.CENTER);

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

        pane.add(updateBtn, 0, 9, 3, 1);

        pane.setHgap(12);
        pane.setVgap(12);

        pane.setPadding(new Insets(8));

        this.getChildren().addAll(pane);

    }

    public void setData(Task task, CheckBoxTreeItem<Task> taskItem) {
        titel.setText(task.getBezeichnung());
        updateTitel(taskItem);
        zeitField.setText(task.getVerbleibendeZeit() + " Tage");
        prioritaetFeld.setValue(task.getPrio());
        bezeichnungField.setText(task.getBezeichnung());
        beschreibungField.setText(task.getBeschreibung());
        dateField.setValue(task.getStartDatum());
        dateFieldEndDate.setValue(task.getAbschlussDatum());
        updateProgressBar(task, taskItem);
    }

    public static void setPriorityColor(TreeItem<Task> item, Node node)
    {
        if(item != null && node !=null)
        {
            if(!item.getValue().isDone())
            {
                switch(item.getValue().getPrio())
                {
                    case Niedrig -> node.setStyle("-fx-text-fill: green");
                    //case Normal -> setStyle("-fx-text-fill: blue");
                    case Hoch -> node.setStyle("-fx-text-fill: orange");
                    case Superhoch -> node.setStyle("-fx-text-fill:  #e53935");
                    default -> node.setStyle("");
                }
            } else
            { node.setStyle("-fx-text-fill: #808080; -fx-stroke: red");}
        }
    }

    public void updateTitel(CheckBoxTreeItem<Task> taskItem)
    {
        setPriorityColor(taskItem, titel);

    }
    public void updateProgressBar(Task task, CheckBoxTreeItem<Task> taskItem) {
        double progress = CustomTreeCell.getAnzahlCheckedTasks(taskItem) / Ziel.getAnzahlSubTAsks(task);
        progressBarZeit.setProgress(progress);
        pi.setProgress(progress);
    }

    public void updateBezeichnung(String t) {
        bezeichnungField.setText(t);
    }

    public void setPfadCss(String pfadCss) {
        this.pfadCss = pfadCss;
    }


}

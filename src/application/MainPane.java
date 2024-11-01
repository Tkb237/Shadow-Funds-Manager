package application;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import chartUI.barChart.BarChartPane;
import util.viewer.Viewer;
import chartUI.lineChart.LineChartPane;
import chartUI.lineChart.LineChartPeriod;
import chartUI.pieChart.PieChartDetailPane;
import chartUI.pieChart.PieChartPane;
import chartUI.pieChart.PieChartPeriod;
import event.ChartDoubleClickedEvent;
import event.CusEventTable;
import event.StateChangedEvent;
import geldVerwaltung.Konto;
import geldVerwaltung.Transaktion;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.web.WebView;
import javafx.scene.chart.Chart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import taskUI.TaskUi;
import util.other.Template;
import util.other.MonthYear;
import util.treeViewSaver.TreeViewSaver;
import taskVerwaltung.Task;
import taskVerwaltung.Ziel;
import util.cusWidget.CustomAlert;
import util.cusWidget.FieldSet;
import transUI.TransaktionStack;
import util.reportGenerator.ReportGenerator;

import static transUI.TransaktionUi.SRC;

public class MainPane extends StackPane {
    private static final String FS_PATH = "./conf/fspath.fs";
    private static final String LUF_PATH = "./conf/luf.fs";
    private static final String VERSION = "1.2.0";
    private CreateAccountUI ca;
    private boolean changed = false; // check if a change occurred in the window (Task, Transactions)
    private VBox leftBox;
    private final HBox topCenterBox = new HBox();
    private final VBox mainVBox = new VBox();
    private final TaskUi rigthBox = new TaskUi();
    private final WebView vw = new WebView();
    private Konto konto;
    private final FileChooser fs = new FileChooser();
    private static String currentCss = "light_theme.css"; // the active css style
    private LineChartPane lineChartPane;
    private PieChartDetailPane pieChartDetailPane;
    private TransaktionStack stack;
    private BarChartPane barChartPane;
    private PieChartPane pieChartPane;

    private final HBox mainBox = new HBox();
    private HBox myChartsBox;
    private final Stage myStage;
    private final Viewer hs; // to show help menu

    private final Viewer gs; // showing chart
    private final FieldSet[] myFieldSets = new FieldSet[3];

    public MainPane(Konto k, Stage s) {

        this.konto = k;
        this.myStage = s;
        this.ca = new CreateAccountUI(myStage);
        ReportGenerator.setKonto(konto);
        setTitle();
        setUpBar();
        setUp();
        updateChart();
        loadPathFileChooser();
        loadIdCounter();
        settingStage();
        loadLastUsedFile();
        addChartsEventListener();
        this.gs = new Viewer(s);
        this.hs = new Viewer(s);

    }

    private void settingStage() {
        // custom what should happen if we try to close the window
        myStage.setOnCloseRequest(event ->
        {
            if (changed) {
                CustomAlert tAlert = new CustomAlert(AlertType.CONFIRMATION);
                tAlert.getButtonTypes().clear();
                tAlert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                tAlert.setContentText("Do you want to save changes ?");
                ButtonType erg = tAlert.showAndWait().orElse(null);
                if (erg == null || erg == ButtonType.NO) {
                    event.consume();
                    savePathFileChooser();
                    saveIdCounter();
                    Platform.exit();
                } else if (erg == ButtonType.YES) {
                    fs.setInitialFileName("SAVE_" + konto.getInhaber().toUpperCase() + "_" + LocalDate.now() + "_" + LocalDateTime.now().getHour() + "-" + LocalDateTime.now().getMinute() + "-" + LocalDateTime.now().getSecond());
                    save(fs.showSaveDialog(myStage));
                    event.consume();
                    savePathFileChooser();
                    saveIdCounter();
                    Platform.exit();
                } else {
                    event.consume();
                }
            }
        });
        // Adapt the window according to the resizing
        myStage.heightProperty().addListener((a, old, newValue) ->
        {
            if (Double.compare((Double) newValue, 735.) < 0) {
                myChartsBox.setManaged(false);
                myChartsBox.setVisible(false);
            } else {
                if (!myChartsBox.isManaged()) {
                    myChartsBox.setManaged(true);
                    myChartsBox.setVisible(true);
                }
            }
        });
        myStage.widthProperty().addListener((a, old, newValue) ->
        {
            if (Double.compare((Double) newValue, 1086.) < 0) {
                rigthBox.setManaged(false);
                rigthBox.setVisible(false);
            } else {
                if (!rigthBox.isManaged()) {
                    rigthBox.setManaged(true);
                    rigthBox.setVisible(true);
                }
            }
        });
    }

    private void saveIdCounter() {
        // save the static attribut of the class "Konto"
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./conf/.ids.fs"))) {
            int ids = Konto.getIdCounter();
            oos.writeObject(ids);
        } catch (Exception ignored) {

        }

    }

    private void loadIdCounter() {
        // load the static attribut of the class "Konto"
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./conf/.ids.fs"))) {
            Konto.setIdCounter((int) ois.readObject());
        } catch (Exception ignored) {
        }
    }

    private void changeStyle(String pfad, boolean b) {
        // switch between dark and light theme
        currentCss = pfad;
        CustomAlert.setStyle(getCssForCusAlert());
        myStage.getScene().getStylesheets().clear();
        myStage.getScene().getStylesheets().add(getClass().getResource(SRC+pfad).toExternalForm());

        for (FieldSet fSet : myFieldSets) {
            fSet.setCusStyle(b);
        }
        stack.setStyleTrUI(pfad);
        rigthBox.setTaskInfoStyle(pfad);
    }

    public static String getCssForCusAlert()
    {
        return currentCss.contains("dark") ? "cusAlertDark.css" : "cusAlertLight.css";
    }

    private void setUpBar() {
        // config the menu bar
        MenuBar menuBar = new MenuBar();
        ImageView[] imageViews = {new ImageView(getClass().getResource("/icons/icons8-registration-64.png").toExternalForm()),
                new ImageView(getClass().getResource("/icons/icons8-save-64.png").toExternalForm()),
                new ImageView(getClass().getResource("/icons/icons8-load-96.png").toExternalForm())};

        for (ImageView e : imageViews) {
            e.setFitWidth(20);
            e.setFitHeight(20);
        }

        Menu fileMenu = new Menu("File");
        fileMenu.setId("filemenu");
        Menu moreMenu = new Menu("More");
        MenuItem newMenu = new MenuItem("New", imageViews[0]);
        MenuItem saveMenu = new MenuItem("Save", imageViews[1]);
        MenuItem loadMenu = new MenuItem("Load", imageViews[2]);

        newMenu.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        saveMenu.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        loadMenu.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));


        fileMenu.getItems().addAll(newMenu, new SeparatorMenuItem(), saveMenu, new SeparatorMenuItem(), loadMenu);

        Menu themeMenu = new Menu("Theme");
        CheckMenuItem darkMenu = new CheckMenuItem("Dark");
        CheckMenuItem lightMenu = new CheckMenuItem("Light");

        lightMenu.setSelected(true);

        themeMenu.getItems().addAll(darkMenu, new SeparatorMenuItem(), lightMenu);

        menuBar.getMenus().addAll(fileMenu, themeMenu, moreMenu);

        mainVBox.getChildren().addAll(menuBar);
        mainVBox.setSpacing(4);

        //MenuItem settings = new MenuItem("Settings");
        MenuItem infoMenu = new MenuItem("Info");
        Menu chartsMenu = new Menu("Charts");
        MenuItem lineChartPeriodMenu = new MenuItem("LC Period");
        MenuItem pieChartPeriodMenu = new MenuItem("PC Period");
        MenuItem reportMenu = new MenuItem("Report");
        MenuItem helpMenu = new MenuItem("Help");


        //
        chartsMenu.getItems().addAll(lineChartPeriodMenu, pieChartPeriodMenu);
        moreMenu.getItems().addAll(chartsMenu, reportMenu,infoMenu, helpMenu);
        //

        helpMenu.setOnAction(e ->
        {
            try {
                String toShow = Template.help;
                vw.getEngine().loadContent(toShow);
                hs.setWidth(400);
                hs.setContent(vw, "Help" ,"icons8-hilfe-64.png");
                updateStyleShower();
            }
            catch (Exception ignored)
            {

            }
        }) ;
        // generate a report
        reportMenu.setOnAction(e -> (new ReportGenerator()).generate(MonthYear.from(LocalDate.now())));
        //
        lineChartPeriodMenu.setOnAction( event ->
                {
                    gs.setContent( new LineChartPeriod(konto));
                    updateStyleShower();
                });

        pieChartPeriodMenu.setOnAction( event ->
        {
            gs.setContent( new PieChartPeriod(konto));
            updateStyleShower();
        });
        // config the behavior in case of a click
        infoMenu.setOnAction(e ->
        {
                CustomAlert cAlert = new CustomAlert(AlertType.INFORMATION);
                cAlert.setTitle("Info");
                cAlert.setHeaderText("Welcome to");
                cAlert.getDialogPane().setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-font-style: italic");
                cAlert.setContentText("Shadow Funds Manager " + VERSION);
                cAlert.show();
        });

        saveMenu.setOnAction(e ->
        {
            fs.setInitialFileName("SAVE_" + konto.getInhaber().toUpperCase() + "_" + LocalDate.now() + "_" + LocalDateTime.now().getHour() + "-" + LocalDateTime.now().getMinute() + "-" + LocalDateTime.now().getSecond());
            save(fs.showSaveDialog(myStage));
        });

        loadMenu.setOnAction(e -> load(fs.showOpenDialog(myStage)));

        newMenu.setOnAction(e ->
        {
            // create a new account and delete all tasks
            ca.custom(currentCss);
            ca.showAndWait();
            if (ca.getKonto() != null) {
                konto = ca.getKonto();
                setTitle();
                update(konto);
                rigthBox.getTreeRoot().getChildren().clear();
                ca = new CreateAccountUI(myStage);
            }

        });

        darkMenu.setOnAction(e ->
                {
                    if (darkMenu.isSelected())
                    {
                        changeStyle("dark_theme.css", false);
                        lightMenu.setSelected(false);
                    }
                    else darkMenu.setSelected(true);
                }
        );
        lightMenu.setOnAction(e ->
                {
                    if (lightMenu.isSelected())
                    {
                        changeStyle("light_theme.css", true);
                        darkMenu.setSelected(false);
                    }
                    else lightMenu.setSelected(true);
                }
        );

    }
    private void loadWithoutDialog(File file, Boolean show)
    {
        // load the state of the table and the task manager
        try {
            if (file != null) {
                fs.setInitialDirectory(file.getParentFile());
            }
            try (ObjectInputStream fis = new ObjectInputStream(new FileInputStream(file))) {
                Object[] savedObjects = (Object[]) fis.readObject();
                konto = (Konto) savedObjects[0];
                update(konto);
                rigthBox.getMyTree().setRoot(new CheckBoxTreeItem<Task>(new Ziel("Null")));
                ((Ziel) savedObjects[1]).getTasks().getTodos().forEach(e -> rigthBox.addTreeTask(e));
                Transaktion.setIdCounter((int) savedObjects[2]);
                loadIdCounter();
                loadPathFileChooser();
                saveLastUsedFile(file);
                savePathFileChooser();
                setTitle();
                if (!show)
                {
                    CustomAlert alert = new CustomAlert(AlertType.INFORMATION);
                    alert.setContentText("Loaded with success");
                    alert.showAndWait();
                }
                updateChanged();
            }
        } catch (StreamCorruptedException e) {
            CustomAlert alert = new CustomAlert(AlertType.ERROR);
            alert.setContentText("Datei nicht geöffnet");
            alert.showAndWait();
        } catch (Exception ignored) {
        }
    }
    private void load(File file)
    {
        loadWithoutDialog(file, false);
    }

    private void save(File file) {
        // save the state of the table and the task manager
        try {
            if (file != null) {
                fs.setInitialDirectory(file.getParentFile());
            }
            try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(file))) {
                Task t = TreeViewSaver.save(rigthBox.getMyTree());
                Object[] saveObjects = {konto, t, Transaktion.getIdCounter()};
                fos.writeObject(saveObjects);
                saveIdCounter();
                savePathFileChooser();
                CustomAlert alert = new CustomAlert(AlertType.INFORMATION);
                saveLastUsedFile(file);
                alert.setContentText("Saved with sucess ");
                alert.showAndWait();
                updateChanged();
            } catch (StreamCorruptedException e) {
                CustomAlert alert = new CustomAlert(AlertType.ERROR);
                alert.setContentText("Datei nicht geöffnet");
                alert.showAndWait();
            }
        } catch (Exception ignored) {
        }

    }

    private void createConfDirectory()
    {
        File theDir = new File("./conf");
        if (!theDir.exists()) {
            theDir.mkdirs();
            // mask the configuration directory
        }
        theDir = new File("./report");
        if (!theDir.exists()) {
            theDir.mkdirs();
            // mask the configuration directory
        }
    }

    private void setUp() {
        // set tup the main box
        createConfDirectory();
        mainVBox.getChildren().addAll(mainBox);

        setPrefWidth(Double.MAX_VALUE);
        getChildren().add(mainVBox);

        leftBox = new VBox();
        VBox centerBox = new VBox();

        leftBox.setMinWidth(150);
        leftBox.setMaxWidth(150);
        leftBox.setAlignment(Pos.CENTER);

        mainBox.getChildren().addAll(centerBox, rigthBox);
        mainBox.setSpacing(8);
        myChartsBox = new HBox();

        lineChartPane = new LineChartPane(konto);
        pieChartDetailPane = new PieChartDetailPane(konto);
        stack = new TransaktionStack(myStage, konto);
        barChartPane = new BarChartPane(konto);
        pieChartPane = new PieChartPane(konto);

        topCenterBox.setSpacing(10);
        topCenterBox.getChildren().addAll(leftBox, stack);
        topCenterBox.setMinWidth(400);

        HBox.setHgrow(centerBox, Priority.ALWAYS);
        HBox.setHgrow(stack, Priority.ALWAYS);
        VBox.setVgrow(topCenterBox, Priority.ALWAYS);
        VBox.setVgrow(mainBox, Priority.ALWAYS);

        myChartsBox.getChildren().addAll(barChartPane, lineChartPane, pieChartDetailPane, pieChartPane);

        setUpLeftBox(lineChartPane, pieChartDetailPane, stack);

        centerBox.getChildren().addAll(topCenterBox, myChartsBox);
        //centerBox.getChildren().addAll(topCenterBox);

        centerBox.setSpacing(10);

        stack.setId("stack");
        leftBox.setId("leftbox");
        centerBox.setId("centerbox");
        topCenterBox.setId("topcenterbox");
        rigthBox.setId("rigthbox");
        myChartsBox.setId("chartbox");
        this.setId("mainBox");
        //
        fs.getExtensionFilters().add(new FileChooser.ExtensionFilter("Save", "*.sfm"));
        //
        addChangeListener();
        //gs.getScene().getStylesheets().add(getClass().getResource("css/"+currentCss).toExternalForm());
    }

    private void loadLastUsedFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(LUF_PATH))) {
            loadWithoutDialog((File) ois.readObject(), true);
        } catch (Exception ignored) {
        }
    }

    private void saveLastUsedFile(File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LUF_PATH))) {
            oos.writeObject(file);
        } catch (Exception ignored) {
        }
    }

    private void savePathFileChooser() {
        // save the last path of the file chooser
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FS_PATH))) {
            oos.writeObject(fs.getInitialDirectory());
        } catch (Exception ignored) {
        }
    }

    private void loadPathFileChooser() {
        // load the last path of the file chooser

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FS_PATH))) {
            fs.setInitialDirectory((File) ois.readObject());
        } catch (Exception ignored) {
        }
    }

    private void setUpLeftBox(LineChartPane lineChartPane, PieChartDetailPane pieChartDetailPane, TransaktionStack stack) {
        VBox fieldSetBox1 = new VBox();
        fieldSetBox1.getChildren().addAll(lineChartPane.getRadioButtons());
        VBox fieldSetBox2 = new VBox();
        fieldSetBox2.getChildren().addAll(pieChartDetailPane.getRadioButtons());
        VBox fieldSetBox3 = new VBox();
        fieldSetBox3.getChildren().addAll(stack.getRadioButtons());
        //
        myFieldSets[0] = new FieldSet("Line Chart", fieldSetBox1);
        myFieldSets[1] = new FieldSet("Circle Chart", fieldSetBox2);
        myFieldSets[2] = new FieldSet("Table Infos", fieldSetBox3);
        //
        leftBox.getChildren().add(myFieldSets[0]);
        leftBox.getChildren().addAll(myFieldSets[1]);
        leftBox.getChildren().addAll(myFieldSets[2]);

        leftBox.getChildren().addAll(stack.addButtons());

        leftBox.setSpacing(3);
        fieldSetBox1.setSpacing(5);
        fieldSetBox2.setSpacing(5);
        fieldSetBox3.setSpacing(5);
    }

    private void updateChart() {
        // is used to update the charts if a change occurs in the table
        stack.addEventHandler(CusEventTable.ADD_EVENT_TYPE, e -> {
            update(konto);
            updateTable();
        });
        stack.addEventHandler(CusEventTable.REMOVE_EVENT_TYPE, e -> {
            update(konto);
            updateTable();
        });
        stack.addEventHandler(CusEventTable.UPDATE_EVENT_TYPE, e -> {
            update(konto);
            updateTable();
        });
    }

    private void updateTable() {
        // is used to update the table if a change occurs in the table
        stack.getRadioButtons().forEach(e -> {
            if (e.isSelected()) {
                e.setSelected(false);
                e.fire();
            }

        });
    }

    private void update(Konto k) {
        ReportGenerator.setKonto(k);
        // update oder reset the main box
        stack.setMyKonto(k);
        stack.getRadioButtons().forEach(e -> {
            if (e.isSelected()) {
                e.setSelected(false);
                e.fire();
            }

        });
        updateCharts(k);

    }

    private void updateCharts(Konto k) {
        // update graphics
        lineChartPane.setMyKonto(k);
        pieChartDetailPane.setMyKonto(k);
        barChartPane.setMyKonto(k);
        pieChartPane.setMyKonto(k);
        barChartPane.updateSerie();
        pieChartPane.setDatatToChart();

        lineChartPane.getRadioButtons().forEach(e ->
        {
            if (e.isSelected()) {
                e.setSelected(false);
                e.fire();
            }
        });

        pieChartDetailPane.getRadioButtons().forEach(e ->
        {
            if (e.isSelected()) {
                e.setSelected(false);
                e.fire();
            }
        });
    }

    private void addChartsEventListener() {
        // to open a window if someone double-clicked on the pane
        addEventHandlerToChart(lineChartPane, lineChartPane.getChartCopy());
        addEventHandlerToChart(pieChartDetailPane, pieChartDetailPane.getChartCopy());
        addEventHandlerToChart(barChartPane, barChartPane.getChartCopy());
        addEventHandlerToChart(pieChartPane, pieChartPane.getChartCopy());
    }

    private void addEventHandlerToChart(Node n, Chart content)
    {
        n.addEventHandler(ChartDoubleClickedEvent.CHART_DB_CLICKED, e ->
        {
            gs.setContent(content);
            updateStyleShower();
        });
    }

    private void setTitle() {
        myStage.setTitle("ShadowFunds Manager - " + konto.getInhaber());
    }

    private void updateStyleShower()
    {
        gs.getScene().getStylesheets().clear();
        gs.getScene().getStylesheets().add(getClass().getResource(SRC+currentCss).toExternalForm());
        hs.getScene().getStylesheets().clear();
        hs.getScene().getStylesheets().add(getClass().getResource(SRC+currentCss).toExternalForm());
    }
    private void addChangeListener() {
        // event handler can't use a function that use the same object
        stack.addEventHandler(StateChangedEvent.CHANGED_EVENT_TYPE, e -> changed = true);
        rigthBox.addEventHandler(StateChangedEvent.CHANGED_EVENT_TYPE, e -> changed = true);
    }

    private void updateChanged()
    {
        changed = false;
        stack.setEmitted(false);
    }

    public static String getCurrentCss() {
        return currentCss;
    }
}

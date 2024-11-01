package chartUI.lineChart;

import java.time.*;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import event.ChartDoubleClickedEvent;
import geldVerwaltung.Eingabe;
import geldVerwaltung.Konto;
import geldVerwaltung.Transaktion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import static util.other.Template.df;

// show information monthly
public class LineChartPeriod extends VBox{

    private Konto myKonto;
    private final RadioButton ausBtn = new RadioButton("Ausgabe");
    private final RadioButton einBtn = new RadioButton("Eingabe");
    private final RadioButton allBtn = new RadioButton("All");
    private LineChart<String, Number> myLineChart2 = new LineChart<String, Number>(new CategoryAxis(), new NumberAxis());

    private final DatePicker vonDatePicker = new DatePicker();
    private final DatePicker bisDatePicker = new DatePicker();

    private final Label vonLabel = new Label("Von: ");
    private final Label bisLabel = new Label("Bis: ");

    private final StackPane chartPane = new StackPane();


    public LineChartPeriod(Konto k)
    {
        myKonto = k;
        setUp();
        configRadioButton();
    }

    public void setUp()
    {
        HBox dateBox = new HBox();
        vonDatePicker.setValue(LocalDate.now().minusDays(30));
        bisDatePicker.setValue(LocalDate.now());

        dateBox.getChildren().addAll(vonLabel, vonDatePicker,
                new Separator(Orientation.VERTICAL), bisLabel,
                bisDatePicker, new Separator(Orientation.VERTICAL), allBtn, einBtn, ausBtn);
        dateBox.setSpacing(15);
        dateBox.setAlignment(Pos.CENTER);
        dateBox.setPadding(new Insets(6));
        dateBox.setMaxWidth(Double.MAX_VALUE);

        for(Node n: dateBox.getChildren())
        {
            HBox.setHgrow(n, Priority.ALWAYS);
        }

        addDatePickerListener(vonDatePicker);
        addDatePickerListener(bisDatePicker);

        VBox.setVgrow(chartPane, Priority.ALWAYS);
        getChildren().addAll(dateBox, chartPane);
        showAllOnChart(vonDatePicker.getValue(), bisDatePicker.getValue());
    }

    private void addEventHandler(Chart chart) {
        // fire an event if it was double-clicked
        chart.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
        {
            if (e.getClickCount() == 2) {
                fireEvent(new ChartDoubleClickedEvent(ChartDoubleClickedEvent.CHART_DB_CLICKED));
            }
        });
    }

    private void configRadioButton() {
        // buttons to tell the chart which information should be showed
        ToggleGroup group = new ToggleGroup();

        ausBtn.setToggleGroup(group);
        einBtn.setToggleGroup(group);
        allBtn.setToggleGroup(group);

        allBtn.setSelected(true);
        ausBtn.setOnAction(e ->
            {
                showAusgabeOnChart(vonDatePicker.getValue(), bisDatePicker.getValue());
            });

        einBtn.setOnAction(e ->
            {
                showEingabeOnChart(vonDatePicker.getValue(), bisDatePicker.getValue());
            });

        allBtn.setOnAction(e ->
            {
                showAllOnChart(vonDatePicker.getValue(), bisDatePicker.getValue());
            }
        );
    }

    public void showEingabeOnChart(LocalDate von, LocalDate bis) {
        myLineChart2 = new LineChart<String, Number>(new CategoryAxis(), new NumberAxis());
        ShowEinAusgabeOnChart(true, new LineChart<String, Number>(new CategoryAxis(), new NumberAxis()), von, bis);
    }

    public void showAusgabeOnChart(LocalDate von, LocalDate bis) {
        myLineChart2 = new LineChart<String, Number>(new CategoryAxis(), new NumberAxis());
        ShowEinAusgabeOnChart(false, new LineChart<String, Number>(new CategoryAxis(), new NumberAxis()), von, bis);

    }

    public void showAllOnChart(LocalDate von, LocalDate bis) {
        myLineChart2 = new LineChart<>(new CategoryAxis(), new NumberAxis());
        LineChart<String, Number> myLineChart = new LineChart<String, Number>(new CategoryAxis(), new NumberAxis());
        ShowEinAusgabeOnChart(true, myLineChart, von, bis);
        ShowEinAusgabeOnChart(false, myLineChart, von, bis);
    }

    private void ShowEinAusgabeOnChart(Boolean bool, LineChart<String, Number> myLineChart, LocalDate von, LocalDate bis) {
        XYChart.Series<String, Number> serie = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> serie2 = new XYChart.Series<String, Number>();

        List<Eingabe> list;

        if (!bool) {
            list = myKonto.getAusgabelist();
            serie.setName("Ausgaben");
            serie2.setName("Ausgaben");


        } else {
            list = myKonto.getEingabelist();
            serie.setName("Eingaben");
            serie2.setName("Eingaben");
        }

        myLineChart.getData().add(serie);
        myLineChart2.getData().add(serie2);

        // get all month sorted
        Set<LocalDate> days = new TreeSet<LocalDate>();
        list.forEach(s ->
        {
            if(isAfter(s.getDate(), von) && isBefore(s.getDate(), bis))
                days.add(s.getDate());
        });

        // get data for the data series
        for (LocalDate day : days) {
            float count = list.stream().filter(s -> (s.getDate().equals(day))).map(Transaktion::getBetrag)
                    .reduce(0.f, Float::sum);
            Data<String, Number> data = new Data<String, Number>(day.toString().replace("--",""), count);
            Data<String, Number> data2 = new Data<String, Number>(day.toString(), count);

            serie2.getData().add(data2);
            serie.getData().add(data);

            Tooltip.install(data.getNode(), new Tooltip(df.format(data.getYValue())));
            Tooltip.install(data2.getNode(), new Tooltip(df.format(data2.getYValue())));
        }

        chartPane.getChildren().clear();
        chartPane.getChildren().add(myLineChart);
        addEventHandler(myLineChart);

    }

    public boolean isAfter(LocalDate date, LocalDate compareDate)
    {
        // check if date is after oder equal compareDate
        return date.isAfter(compareDate) || date.isEqual(compareDate);
    }
    public boolean isBefore(LocalDate date, LocalDate compareDate)
    {
        // check if date is before oder equal compareDate

        return date.isBefore(compareDate) || date.isEqual(compareDate);
    }
    private ObservableList<RadioButton> getRadioButtons() {
        return FXCollections.observableArrayList(allBtn, einBtn, ausBtn);
    }

    public void addDatePickerListener(DatePicker datePicker)
    {
        datePicker.valueProperty().addListener((prop, old, newValue) ->
        {
            getRadioButtons().forEach(btn ->
            {
                if(btn.isSelected())
                {
                    btn.setSelected(false);
                    btn.fire();
                }
            });
        });
    }

    public LineChart<String, Number> getChartCopy() {
        // it isn't a copy
        return myLineChart2;
    }

}

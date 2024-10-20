package chartUI;

import java.text.DecimalFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import event.ChartDoubleClickedEvent;
import geldVerwaltung.Eingabe;
import geldVerwaltung.Konto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

// show information monthly
public class LineChartPane extends StackPane {

    private Konto myKonto;
    private RadioButton ausBtn = new RadioButton("Ausgabe");
    private RadioButton einBtn = new RadioButton("Eingabe");
    private RadioButton allBtn = new RadioButton("All");
    private LineChart<String, Number> myLineChart2 = new LineChart<String, Number>(new CategoryAxis(), new NumberAxis());

    public LineChartPane(Konto k) {
        myKonto = k;
        setUp();
        configRadioButton();
    }

    public void setUp() {
        showAllOnChart();
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
            showAusgabeOnChart();
        });
        einBtn.setOnAction(e ->
        {
            showEingabeOnChart();
        });
        allBtn.setOnAction(e ->
                {
                    showAllOnChart();
                }
        );
    }

    public void showEingabeOnChart() {
        myLineChart2 = new LineChart<String, Number>(new CategoryAxis(), new NumberAxis());
        ShowEinAusgabeOnChart(true, new LineChart<String, Number>(new CategoryAxis(), new NumberAxis()));

    }

    public void showAusgabeOnChart() {
        myLineChart2 = new LineChart<String, Number>(new CategoryAxis(), new NumberAxis());
        ShowEinAusgabeOnChart(false, new LineChart<String, Number>(new CategoryAxis(), new NumberAxis()));

    }

    public void showAllOnChart() {
        myLineChart2 = new LineChart<String, Number>(new CategoryAxis(), new NumberAxis());
        LineChart<String, Number> myLineChart = new LineChart<String, Number>(new CategoryAxis(), new NumberAxis());
        ShowEinAusgabeOnChart(true, myLineChart);
        ShowEinAusgabeOnChart(false, myLineChart);
    }

    private void ShowEinAusgabeOnChart(Boolean bool, LineChart<String, Number> myLineChart) {
        DecimalFormat df = new DecimalFormat("#.00");
        XYChart.Series<String, Number> serie = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> serie2 = new XYChart.Series<String, Number>();

        List<Eingabe> list;

        if (!bool) {
            list = myKonto.getAusgabelist();
            serie.setName("Monatliche Ausgabe");
            serie2.setName("Monatliche Ausgabe");


        } else {
            list = myKonto.getEingabelist();
            serie.setName("Monatliche Eingabe");
            serie2.setName("Monatliche Eingabe");
        }

        // get all month sorted
        Set<Month> months = new TreeSet<Month>();
        list.forEach(s -> months.add(s.getDate().getMonth()));
        List<Data<String, Number>> seriesData = new ArrayList<Data<String, Number>>();
        List<Data<String, Number>> seriesData2 = new ArrayList<Data<String, Number>>();

        // get data for the data series
        for (Month month : months) {
            float count = list.stream().filter(s -> (s.getDate().getMonth() == month)).map(s -> s.getBetrag())
                    .reduce(0.f, (a, b) -> a + b);
            Data<String, Number> data = new Data<String, Number>(month.toString().substring(0, 3), count);
            Data<String, Number> data2 = new Data<String, Number>(month.toString().substring(0, 3), count);

            Tooltip tt = new Tooltip(df.format(data.getYValue()));
            Tooltip.install(data.getNode(), tt);
            Tooltip.install(data2.getNode(), new Tooltip(df.format(data.getYValue())));

            seriesData2.add(data2);
            seriesData.add(data);
        }
        serie.setData(FXCollections.observableArrayList(seriesData));
        serie2.setData(FXCollections.observableArrayList(seriesData2));
        getChildren().clear();
        getChildren().add(myLineChart);
        addEventHandler(myLineChart);
        myLineChart.getData().add(serie);
        myLineChart2.getData().add(serie2);
    }

    public ObservableList<RadioButton> getRadioButtons() {
        return FXCollections.observableArrayList(allBtn, einBtn, ausBtn);
    }

    public void setMyKonto(Konto myKonto) {
        this.myKonto = myKonto;
    }

    public LineChart<String, Number> getChartCopy() {
        // it isn't a copy
        return myLineChart2;
    }

}

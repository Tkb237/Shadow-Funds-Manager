package chartUI;

import java.util.ArrayList;
import java.util.List;

import event.ChartDoubleClickedEvent;
import geldVerwaltung.Konto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

//show the difference summe of every category
public class BarChartPane extends StackPane {
    private BarChart<String, Number> myBart;
    private Konto myKonto;
    private BarChart<String, Number> secondBart; // a "copy" of a chart to show it in the Graph Shower

    public BarChartPane(Konto k) {
        myKonto = k;
        myBart = new BarChart<String, Number>(new CategoryAxis(), new NumberAxis());
        secondBart = new BarChart<String, Number>(new CategoryAxis(), new NumberAxis());

        setUp();
        addEventHandler();
    }

    public void setUp() {
        getChildren().add(myBart);
        myBart.setTitle("Summen");
        updateSerie();
    }

    public void updateSerie() {
        // add data to the chart from the account(konto)
        myBart.getData().clear();
        secondBart.getData().clear();

        // long story
        List<Data<String, Number>> datas = new ArrayList<>();
        List<Data<String, Number>> datas2 = new ArrayList<>();

        float eingabesumme = myKonto.getEingabelist().stream().map(s -> s.getBetrag()).reduce(0.0f, (a, b) -> a + b) + myKonto.getSaldo();
        float ausgabeSumme = myKonto.getAusgabelist().stream().map(s -> s.getBetrag()).reduce(0.0f, (a, b) -> a + b);
        float darlehenSumme = myKonto.getDarlehenList().stream().map(s -> s.getBetrag()).reduce(0.0f, (a, b) -> a + b) + myKonto.getDarlehen();
        float schuldenSumme = myKonto.getDarlehenList().stream().map(s -> s.getBetrag()).reduce(0.0f, (a, b) -> a + b) + myKonto.getSchuld();
        float[] summen = {eingabesumme, ausgabeSumme, darlehenSumme, schuldenSumme};
        for (float f : summen) {
            datas.add(new Data<>("", f));
            datas2.add(new Data<>("", f));
        }

        String[] name = {"Ausgabe", "Eingabe", "Darlehen", "Schulden"};
        // myBart.getData().add(new XYChart.Series<String, Number>(FXCollections.observableArrayList(datas)));
        //myBart.getData().get(0).setName("Summe"); */
        int count = 0;
        for (Data<String, Number> d : datas) {
            ObservableList<Data<String, Number>> list = FXCollections.observableArrayList();
            list.add(d);
            XYChart.Series<String, Number> data = new XYChart.Series<String, Number>(name[count], list);
            myBart.getData().add(data);
            count++;
        }
        count = 0;
        for (Data<String, Number> d : datas2) {
            ObservableList<Data<String, Number>> list = FXCollections.observableArrayList();
            list.add(d);
            secondBart.getData().add(new XYChart.Series<String, Number>(name[count], list));
            count++;
        }
        myBart.getData().forEach(d ->
                Tooltip.install(d.getData().get(0).getNode(), new Tooltip(d.getData().get(0).getYValue().toString())));
        secondBart.getData().forEach(d ->
                Tooltip.install(d.getData().get(0).getNode(), new Tooltip(d.getData().get(0).getYValue().toString())));
    }


    private void addEventHandler() {
        // fire a event if a chart is double clicked
        myBart.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
        {
            if (e.getClickCount() == 2) {
                fireEvent(new ChartDoubleClickedEvent(ChartDoubleClickedEvent.CHART_DB_CLICKED));
            }
        });
    }

    public void setMyKonto(Konto myKonto) {
        this.myKonto = myKonto;
    }

    public BarChart<String, Number> getChartCopy() {
        // it isn't a copy
        return secondBart;
    }
}

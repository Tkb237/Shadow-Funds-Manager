package chartUI.pieChart;

import java.util.List;
import java.util.TreeSet;

import event.ChartDoubleClickedEvent;
import geldVerwaltung.Konto;
import geldVerwaltung.Transaktion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import static util.other.Template.df;

//show proportions for every category
public class PieChartDetailPane extends VBox {

    private PieChart myPieChart;
    private PieChart secondPieChart; // a "copy" of a chart for the graph shower read comment from the another Charts
    private Konto myKonto;
    private final RadioButton ausgabeChoice = new RadioButton("Ausgabe");
    private final RadioButton eingabeChoice = new RadioButton("Eingabe");
    private final RadioButton darlehenChoice = new RadioButton("Darlehen");
    private final RadioButton schuldenChoice = new RadioButton("Schulden");

    public PieChartDetailPane(Konto k) {
        myKonto = k;
        setUp();
        addEventHandler();
        addToggleButton();
    }

    private void setUp() {

        myPieChart = new PieChart();
        secondPieChart = new PieChart();
        myPieChart.setTitle("Eingabe Anteile");
        myPieChart.setLabelsVisible(false);
        secondPieChart.setTitle("Eingabe Anteile");
        secondPieChart.setLabelsVisible(true);
        getChildren().add(myPieChart);
        // The chart should take all the free place
        VBox.setVgrow(myPieChart, Priority.ALWAYS);
        //HBox.setHgrow(theChart.getChartCopy(), Priority.ALWAYS);
        myPieChart.setMaxHeight(Double.MAX_VALUE);
        myPieChart.setMaxWidth(Double.MAX_VALUE);
        myPieChart.setLegendSide(Side.BOTTOM);
        secondPieChart.setLegendSide(Side.LEFT);
        show();

    }

    public void show() {
        setDataToChart(myKonto.getEingabelist());

    }

    private void addEventHandler() {
        myPieChart.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
        {
            if (e.getClickCount() == 2) {
                fireEvent(new ChartDoubleClickedEvent(ChartDoubleClickedEvent.CHART_DB_CLICKED));
            }
        });
    }

    private void addToggleButton() {
        ToggleGroup gp = new ToggleGroup();

        ausgabeChoice.setToggleGroup(gp);
        eingabeChoice.setToggleGroup(gp);
        darlehenChoice.setToggleGroup(gp);
        schuldenChoice.setToggleGroup(gp);
        eingabeChoice.setSelected(true);


        ausgabeChoice.setOnAction(e ->
        {
            setDataToChart(myKonto.getAusgabelist());
        });
        eingabeChoice.setOnAction(e ->
        {
            setDataToChart(myKonto.getEingabelist());
        });
        darlehenChoice.setOnAction(e ->
        {
            setDataToChart(myKonto.getDarlehenList());
        });
        schuldenChoice.setOnAction(e ->
        {
            setDataToChart(myKonto.getSchuldenList());
        });

        gp.selectedToggleProperty().addListener((obs, oldV, newV) ->
           {
                if (newV != null) {
                    myPieChart.setTitle(((RadioButton) newV).getText() + " Anteile");
                    secondPieChart.setTitle(((RadioButton) newV).getText() + " Anteile");
                }
            });
    }

    public void setDataToChart(List<? extends Transaktion> list) {
        myPieChart.getData().clear();
        secondPieChart.getData().clear();
        TreeSet<String> bezeichnungen = new TreeSet<String>(list.stream().map(s -> s.getBezeichnung().toLowerCase()).toList());
        float summe = list.stream().map(Transaktion::getBetrag).reduce(0.f, Float::sum);
        ObservableList<Data> datas = FXCollections.observableArrayList();
        ObservableList<Data> seconDatas = FXCollections.observableArrayList();

        for (String bez : bezeichnungen) {
            float anteil = list.stream().filter(s -> s.getBezeichnung().toLowerCase().equals(bez)).map(Transaktion::getBetrag).reduce(0.f, Float::sum) * 100;
            datas.add(new Data(bez.toUpperCase(), anteil / summe));
            seconDatas.add(new Data(bez.toUpperCase(), anteil / summe));
        }
        myPieChart.layout();
        myPieChart.setData(datas);
        secondPieChart.layout();
        secondPieChart.setData(seconDatas);


        setToolTip(myPieChart.getData());
        setToolTip(secondPieChart.getData());
    }

    private void setToolTip(ObservableList<Data> dataList)
    {
        for (Data data : dataList) {
            Node node = data.getNode();
            Tooltip tpTooltip = new Tooltip(data.getName() + ":  " + df.format(data.getPieValue()) + " %");
            Tooltip.install(node, tpTooltip);
        }
    }

    public ObservableList<RadioButton> getRadioButtons() {
        return FXCollections.observableArrayList(eingabeChoice, ausgabeChoice, darlehenChoice, schuldenChoice);
    }

    public void setMyKonto(Konto myKonto) {
        this.myKonto = myKonto;
    }

    public PieChart getChartCopy() {
        return secondPieChart;
    }

    public PieChart getMyPieChart() {
        return myPieChart;
    }
}


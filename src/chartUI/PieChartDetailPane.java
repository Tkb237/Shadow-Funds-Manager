package chartUI;

import java.text.DecimalFormat;
import java.util.List;
import java.util.TreeSet;

import event.ChartDoubleClickedEvent;
import geldVerwaltung.Konto;
import geldVerwaltung.Transaktion;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

//show proportions for every categorie
public class PieChartDetailPane extends VBox {

    private PieChart myPieChart;
    private PieChart secondPieChart; // a "copy" of a chart for the graph shower read comment from the another Charts
    private Konto myKonto;
    private RadioButton ausgabeChoice = new RadioButton("Ausgabe");
    private RadioButton eingabeChoice = new RadioButton("Eingabe");
    private RadioButton darlehenChoice = new RadioButton("Darlehen");
    private RadioButton schuldenChoice = new RadioButton("Schulden");

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
        secondPieChart.setLabelsVisible(false);
        getChildren().add(myPieChart);

        myPieChart.setLegendSide(Side.BOTTOM);
        secondPieChart.setLegendSide(Side.BOTTOM);
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

        gp.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {


                                                    @Override
                                                    public void changed(ObservableValue<? extends Toggle> arg0, Toggle oldV, Toggle newV) {
                                                        if (newV != null) {
                                                            myPieChart.setTitle(((RadioButton) newV).getText() + " Anteile");
                                                            secondPieChart.setTitle(((RadioButton) newV).getText() + " Anteile");
                                                        }
                                                        ;
                                                    }

                                                }

        );

    }

    public void setDataToChart(List<? extends Transaktion> list) {
        DecimalFormat df = new DecimalFormat("#.00");
        myPieChart.getData().clear();
        secondPieChart.getData().clear();
        TreeSet<String> bezeichnungen = new TreeSet<String>(list.stream().map(s -> s.getBezeichnung().toLowerCase()).toList());
        float summe = list.stream().map(s -> s.getBetrag()).reduce(0.f, (a, b) -> a + b);
        ObservableList<Data> datas = FXCollections.observableArrayList();
        ObservableList<Data> seconDatas = FXCollections.observableArrayList();

        for (String bez : bezeichnungen) {
            float anteil = list.stream().filter(s -> s.getBezeichnung().toLowerCase().equals(bez)).map(s -> s.getBetrag()).reduce(0.f, (a, b) -> a + b) * 100;
            datas.add(new Data(bez.toUpperCase(), anteil / summe));
            seconDatas.add(new Data(bez.toUpperCase(), anteil / summe));
        }
        myPieChart.layout();
        myPieChart.setData(datas);
        secondPieChart.layout();
        secondPieChart.setData(seconDatas);

        setToolTip(myPieChart.getData(), df);
        setToolTip(secondPieChart.getData(), df);
    }

    private void setToolTip(ObservableList<Data> dataList, DecimalFormat df)
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

    public void clear() {
        myPieChart.getData().clear();
        secondPieChart.getData().clear();
    }

    public PieChart getChartCopy() {
        return secondPieChart;
    }
}


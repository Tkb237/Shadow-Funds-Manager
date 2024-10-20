package chartUI;


import java.text.DecimalFormat;

import event.ChartDoubleClickedEvent;
import geldVerwaltung.Konto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.Chart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

// show the proportions
public class PieChartPane extends StackPane {
    private PieChart myPieChart;
    private PieChart seccondPieChart;
    private Konto myKonto;

    public PieChartPane(Konto k) {
        myKonto = k;
        setUp();
        addEventHandler();
    }

    private void setUp() {

        myPieChart = new PieChart();
        seccondPieChart = new PieChart();
        //myPieChart.setLegendVisible(false);
        myPieChart.setLabelsVisible(false);
        myPieChart.setTitle("Anteile");
        seccondPieChart.setLabelsVisible(false);
        seccondPieChart.setTitle("Anteile");
        setDatatToChart();
        myPieChart.setLegendSide(Side.BOTTOM);
        myPieChart.setTitleSide(Side.TOP);
        seccondPieChart.setLegendSide(Side.BOTTOM);
        seccondPieChart.setTitleSide(Side.TOP);
        //
        getChildren().add(myPieChart);
    }

    private void addEventHandler() {
        myPieChart.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
        {
            if (e.getClickCount() == 2) {
                fireEvent(new ChartDoubleClickedEvent(ChartDoubleClickedEvent.CHART_DB_CLICKED));
            }
        });
    }

    public void setDatatToChart() {
        float numberOfTrasnations = myKonto.getAnzahlTransactions() / 100.f;
        ObservableList<PieChart.Data> datas = FXCollections.observableArrayList(
                new PieChart.Data("Eingabe", myKonto.getAnzahlEingaben() / numberOfTrasnations),
                new PieChart.Data("Ausgabe", myKonto.getAnzahlAusgaben() / numberOfTrasnations),
                new PieChart.Data("Darlehen", myKonto.getAnzahlDarlehen() / numberOfTrasnations),
                new PieChart.Data("Schuld", myKonto.getAnzahlSchulden() / numberOfTrasnations)
        );
        ObservableList<PieChart.Data> seconDatas = FXCollections.observableArrayList(
                new PieChart.Data("Eingabe", myKonto.getAnzahlEingaben() / numberOfTrasnations),
                new PieChart.Data("Ausgabe", myKonto.getAnzahlAusgaben() / numberOfTrasnations),
                new PieChart.Data("Darlehen", myKonto.getAnzahlDarlehen() / numberOfTrasnations),
                new PieChart.Data("Schuld", myKonto.getAnzahlSchulden() / numberOfTrasnations)
        );
        myPieChart.setData(datas);
        seccondPieChart.setData(seconDatas);
        addToolTip();

    }

    protected void addToolTip() {
        DecimalFormat df = new DecimalFormat("#.00");
        for (PieChart.Data data : myPieChart.getData()) {
            Node node = data.getNode();
            Tooltip tpTooltip = new Tooltip(data.getName() + ":  " + df.format(data.getPieValue()) + " %");
            Tooltip.install(node, tpTooltip);
        }
        for (PieChart.Data data : seccondPieChart.getData()) {
            Node node = data.getNode();
            Tooltip tpTooltip = new Tooltip(data.getName() + ":  " + df.format(data.getPieValue()) + " %");
            Tooltip.install(node, tpTooltip);
        }
    }

    public void setMyKonto(Konto myKonto) {
        this.myKonto = myKonto;
    }

    public Chart getChartCopy() {
        // TODO Auto-generated method stub
        return seccondPieChart;
    }
}

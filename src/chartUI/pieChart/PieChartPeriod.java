package chartUI.pieChart;

import application.MainPane;
import geldVerwaltung.Konto;
import geldVerwaltung.Transaktion;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import transUI.CustomAlert;
import util.other.MonthYear;
import util.reportGenerator.ReportGenerator;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

public class PieChartPeriod extends VBox
{
    private final PieChartDetailPane theChart;
    private final ComboBox<Integer> yearPicker;
    private final ComboBox<Month> monthPicker;
    private final RadioButton monthButton = new RadioButton("Month");
    private final RadioButton yearButton = new RadioButton("Year");
    private final Button reportButton = new Button("Generate Report");
    private final Konto myKonto;

    public PieChartPeriod(Konto k)
    {
        theChart = new PieChartDetailPane(k);
        monthPicker = new ComboBox<>();
        yearPicker = new ComboBox<>();
        this.myKonto = k;
        setUp();
        settingMonthYearPicker();
    }

    private void settingMonthYearPicker()
    {
        monthPicker.getItems().setAll(Month.values());

        yearPicker.getItems().addAll(myKonto.getTransaktions().stream().map(t -> t.getDate().getYear())
                        .distinct().toList());

        monthPicker.getSelectionModel().select(LocalDate.now().getMonth());
        yearPicker.getSelectionModel().selectLast();

        monthPicker.getSelectionModel().selectedItemProperty().addListener
                ((observable, old, newValue) -> updateAfterChange());
        yearPicker.getSelectionModel().selectedItemProperty().addListener
               ((observable, old, newValue) -> updateAfterChange());
    }

    private void updateAfterChange()
    {
        theChart.getRadioButtons().forEach(btn ->
        {
            if(btn.isSelected())
            {
                btn.setSelected(false);
                btn.fire();
            }
        });
    }
    private void setUp()
    {
        theChart.getMyPieChart().setLegendSide(Side.LEFT);
        theChart.getMyPieChart().isLegendVisible();

        theChart.getMyPieChart().setLabelsVisible(true);
        theChart.getMyPieChart().setTitleSide(Side.BOTTOM);

        HBox container = new HBox();
        container.setPadding(new Insets(10));

        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(monthButton, yearButton);
        tg.selectedToggleProperty().addListener((observable, old, newValue) -> updateAfterChange());

        yearButton.setSelected(true);

        container.getChildren().addAll(new Label("Month:"), monthPicker, new Separator(Orientation.VERTICAL),
                new Label("Year:"), yearPicker, new Separator(Orientation.VERTICAL), monthButton, yearButton,
                new Separator(Orientation.VERTICAL));

        container.getChildren().addAll(theChart.getRadioButtons());
        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);
        container.setMaxWidth(Double.MAX_VALUE);

        for(Node n: container.getChildren())
        {
            HBox.setHgrow(n, Priority.ALWAYS);
        }
        getChildren().addAll(container, theChart.getMyPieChart(), reportButton);
        confRadioButtonPieChart();

        reportButton.setStyle("-fx-background-color: orangered; -fx-font-size: 150%;");

        reportButton.setOnAction( e ->
        {
            ReportGenerator rg = new ReportGenerator();
            if(monthButton.isSelected())
            {
                MonthYear my = MonthYear.of(monthPicker.getValue(), yearPicker.getValue());;
                rg.generate(my);
            }
            else rg.generate(Year.of(yearPicker.getValue()));


        });
    }

    private List<? extends  Transaktion> generateList(List<? extends  Transaktion> list)
    {
        // generate a list for the pie chart
        if(monthButton.isSelected())
        {
            return list.stream().
                    filter( t -> MonthYear.from(t.getDate()).
                            equals(MonthYear.of(monthPicker.getValue(),
                                    Integer.parseInt(String.valueOf(yearPicker.getValue()))))).toList();
        }
        else
        {
           return list.stream().
                    filter( t -> Year.from(t.getDate()).getValue() == yearPicker.getValue()).toList();
        }
    }

    private void confRadioButtonPieChart()
    {
        ObservableList<RadioButton> buttons = theChart.getRadioButtons();

        buttons.get(0).setOnAction(e ->
                theChart.setDataToChart(generateList(myKonto.getEingabelist())));
        buttons.get(1).setOnAction(e ->
                theChart.setDataToChart(generateList(myKonto.getAusgabelist())));
        buttons.get(2).setOnAction(e ->
                theChart.setDataToChart(generateList(myKonto.getDarlehenList())));
        buttons.get(3).setOnAction(e ->
                theChart.setDataToChart(generateList(myKonto.getSchuldenList())));

    }

}


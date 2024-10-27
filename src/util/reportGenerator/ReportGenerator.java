package util.reportGenerator;

import geldVerwaltung.Konto;
import geldVerwaltung.Transaktion;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import transUI.CustomAlert;
import util.template.Template;
import util.other.MonthYear;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static application.MainPane.cssForCusAlert;

public class ReportGenerator extends Node
{
    private static Konto konto;

    private static String[] generateTable(Object o)
    {
        if( o instanceof MonthYear monthYear)
            return new String[]
                {generateTableRow(getMatchingList(konto.getAusgabelist(), monthYear)),
                        generateTableRow(getMatchingList(konto.getEingabelist(), monthYear)),
                        generateTableRow(getMatchingList(konto.getDarlehenList(), monthYear)),
                        generateTableRow(getMatchingList(konto.getSchuldenList(), monthYear))
                        };
        else if(o instanceof Year year)
        {
            return new String[]
                    {generateTableRow(getMatchingList(konto.getAusgabelist(), year)),
                            generateTableRow(getMatchingList(konto.getEingabelist(), year)),
                            generateTableRow(getMatchingList(konto.getDarlehenList(), year)),
                            generateTableRow(getMatchingList(konto.getSchuldenList(), year))
                    };

        }
        else return new String[]{"","","",""};
    }


    private static List<? extends Transaktion> getMatchingList(List<? extends Transaktion> list, Object o)
    {
        List<? extends Transaktion> listNull = new ArrayList<>();
        if(o instanceof MonthYear my)
        return list.stream().
                filter(transaktion -> MonthYear.from(transaktion.getDate()).equals(my)).toList();
        else if(o instanceof Year year)
            return list.stream().
                    filter(transaktion -> transaktion.getDate().getYear() == year.getValue()).toList();
        return listNull;
    }

    private static String generateTableRow(List<? extends Transaktion> list)
    {
        String tr = "<tr>%s</tr>";
        String td = "<td style=\"padding: 8px; border: 1px solid #ddd; text-align: center\">%s</td>" +
                " <td style=\"padding: 8px; border: 1px solid #ddd; text-align: center\">%s</td> " +
                "<td style=\"padding: 8px; border: 1px solid #ddd; text-align: center\">%s</td>";
        StringBuilder erg = new StringBuilder();
        for (Transaktion t: list)
        {
            erg.append(String.format(tr, String.format(td, t.getBezeichnung(), t.getDate(), t.getBetrag())));
        }
        return  erg.toString();
    }


    public void generate(Object o)
    {
        Thread th = new Thread(() -> generateReport(o));
        th.start();
    }

    private void generateReport(Object o)
    {
        try
        {
            String files = Template.template;
            String[] erg = generateTable(o);
            files =  files.replace("$date", dateToString(o));
            files =  files.replace("$user_name", konto.getInhaber().toLowerCase());
            files =  files.replace("$ausgabe_summe", getSumme(konto.getAusgabelist(), o));
            files =  files.replace("$ausgabe_data", erg[0]);

            files = files.replace("$eingabe_summe", getSumme(konto.getEingabelist(), o));
            files = files.replace("$eingabe_data", erg[1]);

            files = files.replace("$darlehen_summe", getSumme(konto.getDarlehenList(), o));
            files = files.replace("$darlehen_data", erg[2]);

            files = files.replace("$schuld_summe", getSumme(konto.getSchuldenList(), o));
            files = files.replace("$schuld_data", erg[3]);

            String path = "./report/Report_"+dateToString(o).replace(" ","_")+".html";
            File f = new File(path.toUpperCase());
            Files.writeString(Path.of(f.getPath()), files);

            Platform.runLater(() ->
            {
                CustomAlert cs = new CustomAlert(Alert.AlertType.INFORMATION,cssForCusAlert);
                cs.setContentText("Report of " + dateToString(o) + " was generated.");
                cs.show();
            });

        }
        catch (Exception ignored)
        {
        }
    }


    private static String getSumme(List<? extends Transaktion> list, Object o)
    {

        return getMatchingList(list, o).stream().map(Transaktion::getBetrag).
                reduce(Float::sum).orElse(0.f).toString();
    }

    private static String dateToString(Object o)
    {
        if(o instanceof MonthYear m) return m.toString().toLowerCase();
        if(o instanceof Year y) return y.toString().toLowerCase();
        return LocalDate.now().toString();
    }

    public static void setKonto(Konto konto) {
        ReportGenerator.konto = konto;
    }
}

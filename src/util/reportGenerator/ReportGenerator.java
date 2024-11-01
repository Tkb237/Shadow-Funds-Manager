package util.reportGenerator;

import geldVerwaltung.Konto;
import geldVerwaltung.Transaktion;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import util.cusWidget.CustomAlert;
import util.ai.GeminiVertexAI;
import util.other.Template;
import util.other.MonthYear;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ReportGenerator extends Node
{
    private static Konto konto;
    private static Boolean occupied = false;

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
        return list.stream()
            .filter(transaktion -> MonthYear.from(transaktion.getDate()).equals(my))
                .sorted(Comparator.comparing((Transaktion a) -> a.getDate())).toList();
        else if(o instanceof Year year)
            return list.stream().
                    filter(transaktion -> transaktion.getDate().getYear() == year.getValue())
                    .sorted(Comparator.comparing((Transaktion a) -> a.getDate())).toList();
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


    public void generate(Object o){

        if(!occupied)
        {
            Thread th = new Thread(() ->
            {
                synchronized (occupied)
                {
                    occupied = true;
                    generateReport(o);
                    occupied = false;
                }
            });
            th.start();
        }
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
            files = files.replace("$tipp", generateSummaryIA(o));

            String path = "./report/Report_"+dateToString(o).replace(" ","_")+".html";
            File f = new File(path.toUpperCase());
            Files.writeString(Path.of(f.getPath()), files);

            Platform.runLater(() ->
            {
                CustomAlert cs = new CustomAlert(Alert.AlertType.INFORMATION);
                cs.setContentText("Report of " + dateToString(o) + " was generated.");
                cs.show();
            });

        }
        catch (Exception ignored)
        {
        }
    }

    private String generateSummaryIA(Object o)
    {
        String message = "4 Kategorien (Eingabe, Ausgabe, Darlehen(ich bin der Kreditgeber)," +
                " Schuld(ich bin der Kredit nehmer). detaillierte Zusammenfassung ohne Liste und " +
                "tipp zur Verbesserung der Finanzen in einem Html Absatz . \n";
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        sb.append("Bezeichnung | Kategorie | Datum | Betrag\n");
        getMatchingList(konto.getTransaktions(),o).stream().sorted(Comparator.comparing((Transaktion a) -> a.getDate()))
                .forEach(t -> sb.append("\"").append(t.getBezeichnung()).append("\"").append(" ")
                .append(t.getClass().getSimpleName()).append(" ").append(t.getDate()).append(" ")
                .append(String.valueOf(t.getBetrag()).replace(".", ",")).append("$ \n"));
        return GeminiVertexAI.generate(sb.toString());
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

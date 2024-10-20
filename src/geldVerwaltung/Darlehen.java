package geldVerwaltung;

import java.time.LocalDate;

public class Darlehen extends Transaktion {
    /**
     *
     */
    private static final long serialVersionUID = 2005465818241994523L;

    public Darlehen(String Bezeichnung, float betrag) {
        super(Bezeichnung, betrag);
        setDate(LocalDate.now().plusDays(30));
    }

    public Darlehen(String Bezeichnung, float betrag, LocalDate date) {
        super(Bezeichnung, betrag, date);
        setDate(LocalDate.now().plusDays(30));

    }


    public Darlehen(float betrag) {
        super("", betrag);
        setDate(LocalDate.now().plusDays(30));
    }


    public Darlehen(String bezeichung, float betrag, String kategorie) {
        super(bezeichung, betrag);
        setDate(LocalDate.now().plusDays(30));
    }

    public Darlehen(String bezeichung, float betrag, LocalDate rueckgabeDatum, String kategorie) {
        super(bezeichung, betrag, rueckgabeDatum, kategorie);
    }

}

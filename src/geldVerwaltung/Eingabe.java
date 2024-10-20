package geldVerwaltung;

import java.time.LocalDate;

public class Eingabe extends Transaktion {
    /**
     *
     */
    private static final long serialVersionUID = 4422358848033227952L;

    public Eingabe(String bezeichnung, float betrag) {
        super(bezeichnung, betrag);
        setDate(LocalDate.now());
    }

    public Eingabe(String bezeichnung, float betrag, LocalDate date) {
        super(bezeichnung, betrag, date);
    }

}

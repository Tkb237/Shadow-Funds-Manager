package geldVerwaltung;

import java.time.LocalDate;

public class Schuld extends Transaktion {

    /**
     *
     */
    private static final long serialVersionUID = 4510246028618310750L;

    public Schuld(String bezeichung, float betrag) {
        super(bezeichung, betrag);
        setDate(LocalDate.now().plusDays(30));

    }

    public Schuld(String bezeichung, float betrag, LocalDate rueckgabeDatum) {
        super(bezeichung, betrag, rueckgabeDatum);
        setDate(LocalDate.now().plusDays(30));

    }

    public Schuld(String bezeichung, float betrag, String kategorie) {
        super(bezeichung, betrag);
        setDate(LocalDate.now().plusDays(30));
    }

    public Schuld(String bezeichung, float betrag, LocalDate rueckgabeDatum, String kategorie) {
        super(bezeichung, betrag, rueckgabeDatum, kategorie);
    }

    @Override
    public void setBetrag(float betrag) {
        // TODO Auto-generated method stub
        super.setBetrag(-betrag);
    }
}

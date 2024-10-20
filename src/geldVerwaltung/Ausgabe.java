package geldVerwaltung;

import java.time.LocalDate;

public class Ausgabe extends Eingabe {
    /**
     *
     */
    private static final long serialVersionUID = -2723482223031407632L;
    private Prioritaet prio;

    public Ausgabe(String bezeichnung, float betrag, Prioritaet prio) {
        super(bezeichnung, betrag);
        this.prio = prio;
    }

    public Ausgabe(String bezeichnung, float betrag, Prioritaet prio, LocalDate date) {
        super(bezeichnung, betrag, date);
        this.prio = prio;
    }


    public void setPrio(Prioritaet prio) {
        this.prio = prio;
    }

    public Prioritaet getPrio() {
        return prio;
    }

    @Override
    public void setBetrag(float betrag) {
        // TODO Auto-generated method stub
        super.setBetrag(betrag);
    }

}

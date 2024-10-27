package geldVerwaltung;

import util.other.Prioritaet;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Konto implements Serializable {
    /**
     *
     */
    private static int idCounter = 0;

    private static final long serialVersionUID = -1421529887895290331L;
    private float saldo;
    private float schuld;
    private float darlehen;
    private int id;
    private String inhaber;
    private List<Eingabe> einAusgabelist = new ArrayList<Eingabe>();
    private List<Schuld> schuldenList = new ArrayList<Schuld>();
    private List<Darlehen> darlehenList = new ArrayList<Darlehen>();
    private List<Transaktion> transaktions = new ArrayList<Transaktion>();

    public Konto(String inhaber) {
        this.saldo = 0.f;
        this.schuld = 0.f;
        this.darlehen = 0.f;
        this.inhaber = inhaber;
        id = idCounter++;
        Transaktion.setIdCounter(0);

    }

    public Konto(float saldo, float schuld, float darlehen, String inhaber) {
        this.saldo = saldo;
        this.schuld = schuld;
        this.darlehen = darlehen;
        this.inhaber = inhaber;
        id = idCounter++;
        Transaktion.setIdCounter(0);

    }

    private void einAusgeben(Eingabe a) {
        this.saldo += a.getBetrag();
        einAusgabelist.add(a);
        transaktions.add(a);
    }

    private void schulden(Schuld s) {
        this.schuld += s.getBetrag();
        schuldenList.add(s);
        transaktions.add(s);
    }

    private void darlehen(Darlehen d) {
        this.darlehen += d.getBetrag();
        darlehenList.add(d);
        transaktions.add(d);
    }

    private boolean UnEinAusgeben(Eingabe a) {
        this.saldo -= a.getBetrag();
        transaktions.remove(a);
        return einAusgabelist.remove(a);
    }

    private boolean UnSchulden(Schuld s) {
        this.schuld -= s.getBetrag();
        transaktions.remove(s);
        return schuldenList.remove(s);

    }

    private boolean UnDarlehen(Darlehen d) {
        this.darlehen -= d.getBetrag();
        transaktions.remove(d);
        return darlehenList.remove(d);
    }

    public void doTransaktion(Transaktion t) {
        if (t.getClass() == Ausgabe.class || t.getClass() == Eingabe.class) {
            einAusgeben((Eingabe) t);
        } else if (t.getClass() == Darlehen.class) {
            darlehen((Darlehen) t);
        } else if (t.getClass() == Schuld.class) {
            schulden((Schuld) t);
        }
    }

    public void unDoTransaktion(Transaktion t) {
        if (t.getClass() == Ausgabe.class || t.getClass() == Eingabe.class) {
            UnEinAusgeben((Eingabe) t);
        } else if (t.getClass() == Darlehen.class) {
            UnDarlehen((Darlehen) t);
        } else if (t.getClass() == Schuld.class) {
            UnSchulden((Schuld) t);
        }
    }

    private void updateAusgabe(Transaktion t, String bez, String bes, float betrag, LocalDate date, Prioritaet p) {
        if (t.getKategorie().equals("Ausgabe")) {
            for (Transaktion trans : transaktions) {
                if (t.equals(trans)) {
                    trans.setBeschreibung(bes);
                    trans.setBezeichnung(bez);
                    trans.setBetrag(betrag);
                    trans.setDate(date);
                    ((Ausgabe) trans).setPrio(p);
                    einAusgabelist.remove(((Ausgabe) trans));
                    einAusgabelist.add((Ausgabe) trans);
                    break;
                }
            }
        }
    }

    private void updateEingabe(Transaktion t, String bez, String bes, float betrag, LocalDate date) {
        if (t.getKategorie().equals("Eingabe")) {
            for (Transaktion trans : transaktions) {
                if (t.equals(trans)) {
                    trans.setBeschreibung(bes);
                    trans.setBezeichnung(bez);
                    trans.setBetrag(betrag);
                    trans.setDate(date);
                    einAusgabelist.remove(((Eingabe) trans));
                    einAusgabelist.add((Eingabe) trans);
                    break;
                }
            }
        }
    }


    private void updateDarlehen(Transaktion t, String bez, String bes, float betrag, LocalDate date) {
        if (t.getKategorie().equals("Darlehen")) {
            for (Transaktion trans : transaktions) {
                if (t.equals(trans)) {
                    trans.setBeschreibung(bes);
                    trans.setBezeichnung(bez);
                    trans.setBetrag(betrag);
                    trans.setDate(date);
                    darlehenList.remove(((Darlehen) trans));
                    darlehenList.add((Darlehen) trans);
                    break;
                }
            }
        }
    }

    private void updateSchuld(Transaktion t, String bez, String bes, float betrag, LocalDate date) {
        if (t.getKategorie().equals("Schuld")) {
            for (Transaktion trans : transaktions) {
                if (t.equals(trans)) {
                    trans.setBeschreibung(bes);
                    trans.setBezeichnung(bez);
                    trans.setBetrag(betrag);
                    trans.setDate(date);
                    schuldenList.remove(((Schuld) trans));
                    schuldenList.add((Schuld) trans);
                    break;
                }
            }
        }
    }

    public void updateTransaktion(Transaktion t, String bez, String bes, String be, LocalDate date, Prioritaet p) {
        float betrag = Math.abs(Float.parseFloat(be));
        updateAusgabe(t, bez, bes, betrag, date, p);
        updateEingabe(t, bez, bes, betrag, date);
        updateDarlehen(t, bez, bes, betrag, date);
        updateSchuld(t, bez, bes, betrag, date);
    }

    public float getDarlehen() {
        return darlehen;
    }

    public float getSaldo() {
        return saldo;
    }

    public float getSchuld() {
        return schuld;
    }

    public float summe() {
        return getSchuld() + getDarlehen() + getSaldo();
    }

    public String getInhaber() {
        return inhaber;
    }

    public List<Darlehen> getDarlehenList() {
        return darlehenList;
    }

    public List<Schuld> getSchuldenList() {
        return schuldenList;
    }

    public List<Eingabe> getAusgabelist() {
        return einAusgabelist.stream().filter(s -> s instanceof Ausgabe).toList();
    }

    public List<Eingabe> getEingabelist() {
        return einAusgabelist.stream().filter(s -> !(s instanceof Ausgabe)).toList();
    }

    public List<Transaktion> getTransaktions() {
        return transaktions;
    }

    public int getAnzahlTransactions() {
        return transaktions.size();

    }

    public int getAnzahlDarlehen() {
        return darlehenList.size();
    }

    public int getAnzahlSchulden() {
        return schuldenList.size();
    }

    public long getAnzahlAusgaben() {
        return einAusgabelist.stream().filter(s -> s instanceof Ausgabe).count();
    }

    public long getAnzahlEingaben() {
        return einAusgabelist.stream().filter(s -> !(s instanceof Ausgabe)).count();
    }

    public int getId() {
        return id;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Konto.idCounter = idCounter;
    }

}

package geldVerwaltung;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Transaktion implements Serializable {
    /**
     * -4993126018149211537L
     */
    private static final long serialVersionUID = -4993126018149211537L;
    private String bezeichnung; // Name der Transaktion
    private float betrag; // Betrag
    private LocalDate date; // Datum
    private String beschreibung = "Keine Beschreibung";
    protected static String kategorie = "Transaktion";
    private String iD;
    private static int IdCounter = 0;
    ;


    public Transaktion(String bezeichung, float betrag) {
        this.bezeichnung = bezeichung;
        this.betrag = betrag;
        this.iD = "Transaktion Nr: " + IdCounter++;
    }

    public Transaktion(String bezeichung, float betrag, LocalDate rueckgabeDatum) {
        this(bezeichung, betrag);
        this.date = rueckgabeDatum;
    }

    public Transaktion(String bezeichung, float betrag, LocalDate rueckgabeDatum, String kategorie) {
        this(bezeichung, betrag, rueckgabeDatum);
    }

    public void setBetrag(float betrag) {
        this.betrag = betrag;
    }

    public float getBetrag() {
        return betrag;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setDate(LocalDate date) {
        // Diese Methode wird in allen Unterklassen benutzt aber hat eine Unterschiedliche Bedutung
        // Schuld → wann muss ich das Geld zurückgeben
        // Darlehen → wann bekomme ich das Geld
        // Ausgabe → wann wurde das Geld ausgegeben
        // Eingabe → wann wurde das Geld eingegeben
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    @Override
    public String toString() {
        return bezeichnung + " " + kategorie + " " + Double.toString(betrag);
    }

    public static int getIdCounter() {
        return IdCounter;
    }

    public String getiD() {
        return iD;
    }

    public String getKategorie() {
        return this.getClass().getSimpleName();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.getClass() != o.getClass() || o == null) return false;
        Transaktion transaktion = (Transaktion) o;
        return transaktion.getiD().equals(getiD());
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return Objects.hash(bezeichnung, betrag, date, beschreibung, kategorie, iD);
    }

    public static void setIdCounter(int i) {
        IdCounter = i;
    }
}

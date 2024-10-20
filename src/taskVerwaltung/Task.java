package taskVerwaltung;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import geldVerwaltung.Prioritaet;;

public class Task implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static long IdCounter = 0;
    private String bezeichnung;
    private String beschreibung;
    private Prioritaet prio;
    private LocalDate startDatum;
    //int rang;
    private LocalDate abschlussDatum;
    private long id;

    private boolean done = false;
    private boolean indeterminate = false; // neither checked nor unchecked

    public Task(String bezeichnung, Prioritaet prio, LocalDate startdate, LocalDate endDate) {
        this(bezeichnung, prio);
        this.abschlussDatum = endDate;
        this.startDatum = startdate;
    }

    public Task(String bezeichnung) {
        this.bezeichnung = bezeichnung;
        this.prio = Prioritaet.Normal;
        this.startDatum = LocalDate.now();
        this.abschlussDatum = startDatum.plusDays(30);
        this.id = IdCounter++;
    }

    public Task(String bezeichnung, Prioritaet p) {
        this(bezeichnung);
        this.prio = p;
    }


    public Task(String bezeichnung, Prioritaet p, LocalDate end) {
        this(bezeichnung, p, LocalDate.now(), end);
    }

    public Task(String bezeichnung, Prioritaet p, int delai) {
        this(bezeichnung, p);
        setAbschlussDatum(getStartDatum().plusDays(delai));
    }

    public Task(String bezeichnung, Prioritaet p, LocalDate start, int delai) {
        this(bezeichnung, p, delai);
        setStartDatum(start);
        ;
    }


    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public Prioritaet getPrio() {
        return prio;
    }

    public void setPrio(Prioritaet prio) {
        this.prio = prio;
    }
	
	/*public void setRang(int rang) {
		this.rang = rang;
	}*/

    public LocalDate getAbschlussDatum() {
        return abschlussDatum;
    }

    public LocalDate getStartDatum() {
        return startDatum;
    }

    public void setStartDatum(LocalDate startDatum) {
        this.startDatum = startDatum;
    }

    public void setAbschlussDatum(LocalDate abschlussDatum) {
        this.abschlussDatum = abschlussDatum;
    }

    @Override
    public String toString() {
        return bezeichnung;
    }

    public String getIdString() {
        return "Task Nr : " + id;
    }

    public long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }

    public long getDauer() {
        return ChronoUnit.DAYS.between(startDatum, abschlussDatum);
    }
	
	/*private boolean updateStatus() {
		if(!getVerbleibendeZeit().isNegative()) status = true;
		return status;*/

    public long getPastDays() {
        return ChronoUnit.DAYS.between(startDatum, LocalDate.now());
    }

    public long getVerbleibendeZeit() {

        return ChronoUnit.DAYS.between(LocalDate.now(), abschlussDatum);
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean checked) {
        this.done = checked;
    }

    public boolean isIndeterminate() {
        return indeterminate;
    }

    public void setIndeterminate(boolean inderterminate) {
        this.indeterminate = inderterminate;
    }


}


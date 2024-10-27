package taskVerwaltung;

import java.io.Serializable;
import java.util.List;

import util.other.Prioritaet;

public class Ziel extends Task implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public Ziel(Task t) {
        super(t.getBezeichnung(), t.getPrio(), t.getStartDatum(), t.getAbschlussDatum());
        setId(t.getId());
    }

    public Ziel(String bezeichnung) {
        super(bezeichnung);
    }

    public Ziel(String string, Prioritaet niedrig, int i) {
        super(string, niedrig, i);
    }

    private ToDoList schritte = new ToDoList();
    boolean status = false;


    public Task addTask(Task t) {
        return schritte.addTask(t);
    }

    public boolean removeTask(Task t) {
        return schritte.removeTask(t);
    }


    public void removeAllTasks() {
        schritte.removeAll();
    }

    public ToDoList getTasks() {
        return schritte;
    }

    public long getSize() {
        return schritte.getTodos().size();
    }

    public static int getAnzahlSubTAsks(Task t) {
        int counter = 0;
        if (t instanceof Ziel) {
            List<Task> tasks = ((Ziel) t).getTasks().getTodos();
            for (Task s : tasks) {
                counter += Ziel.getAnzahlSubTAsks(s);
            }
            return counter;
        } else return 1;
    }
}

package taskVerwaltung;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final public class ToDoList implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    List<Task> todos = new ArrayList<Task>();

    public ToDoList() {

    }

    Task addTask(Task t) {
        todos.add(t);
        return t;
    }

    boolean removeTask(Task t) {
        return todos.remove(t);
    }

    public void removeAll() {
        todos.clear();
    }

    public void show() {
        for (Task todo : todos) {
            System.out.println(todo.getBezeichnung());
        }
    }

    public List<Task> getTodos() {
        return todos;
    }
}

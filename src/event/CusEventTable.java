package event;

import javafx.event.Event;
import javafx.event.EventType;

public class CusEventTable extends Event {
    // signal changes in the table view
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final EventType<CusEventTable> ADD_EVENT_TYPE = new EventType<>(Event.ANY, "ADD");
    public static final EventType<CusEventTable> REMOVE_EVENT_TYPE = new EventType<>(Event.ANY, "REMOVE");
    public static final EventType<CusEventTable> UPDATE_EVENT_TYPE = new EventType<>(Event.ANY, "UPDATE");

    public CusEventTable(EventType<? extends Event> e) {
        super(e);
    }

}

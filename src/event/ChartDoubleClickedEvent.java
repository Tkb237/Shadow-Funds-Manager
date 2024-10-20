package event;

import javafx.event.Event;
import javafx.event.EventType;

public class ChartDoubleClickedEvent extends Event {
    // check double click
    /**
     *
     */
    private static final long serialVersionUID = -5756713532559486016L;

    public static final EventType<ChartDoubleClickedEvent> CHART_DB_CLICKED = new EventType<>(Event.ANY, "ANY");

    public ChartDoubleClickedEvent(EventType<? extends Event> arg0) {
        super(arg0);
    }

}

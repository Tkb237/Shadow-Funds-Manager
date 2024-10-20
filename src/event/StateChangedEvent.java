package event;

import javafx.event.Event;
import javafx.event.EventType;

public class StateChangedEvent extends Event {
    // it used to check if any change occurs in the complete window
    /**
     *
     */
    public static final EventType<StateChangedEvent> CHANGED_EVENT_TYPE = new EventType<>(Event.ANY, "CHANGED");
    private static final long serialVersionUID = 1L;

    public StateChangedEvent(EventType<? extends Event> e) {
        super(e);
        // TODO Auto-generated constructor stub
    }

}

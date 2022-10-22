package account.model.event;

import account.util.LogEvent;

public class EventBuilder {

    private LogEvent action;
    private String subject;
    private String object;
    private String path;

    private EventBuilder() {
    }

    public static EventBuilder init() {
        return new EventBuilder();
    }

    public EventBuilder withAction(LogEvent action) {
        this.action = action;
        return this;
    }

    public EventBuilder withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EventBuilder withObject(String object) {
        this.object = object;
        return this;
    }

    public EventBuilder withPath(String path) {
        this.path = path;
        return this;
    }

    public Event build() {
        return new Event(action, subject, object, path);
    }
}

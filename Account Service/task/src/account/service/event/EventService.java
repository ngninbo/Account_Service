package account.service.event;

import account.model.event.Event;

import java.util.List;

public interface EventService {

    List<Event> findAll();
    void save(Event event);

}

package account.service.event;

import account.model.event.Event;
import account.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService implements IEventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> findAll() {
        return this.eventRepository.findAll();
    }

    @Override
    public void save(Event event) {
        this.eventRepository.save(event);
    }
}

package account.mapper;

import account.domain.EventDto;
import account.model.event.Event;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    public EventDto toDto(Event event) {
        return new EventDto(event.getAction(), event.getSubject(), event.getObject(), event.getPath());
    }

    public List<EventDto> toList(List<Event> events) {
        return events.isEmpty() ? List.of() : events.stream().map(this::toDto).collect(Collectors.toList());
    }
}

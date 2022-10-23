package account.mapper;

import account.domain.EventDto;
import account.model.event.Event;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    public EventDto toDto(Event event) {
        return EventDto.builder()
                .action(event.getAction())
                .subject(event.getSubject())
                .object(event.getObject())
                .path(event.getPath())
                .build();
    }

    public List<EventDto> toList(List<Event> events) {
        return events.isEmpty() ? List.of() : events.stream().map(this::toDto).collect(Collectors.toList());
    }
}

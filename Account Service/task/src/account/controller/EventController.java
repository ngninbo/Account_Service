package account.controller;

import account.domain.EventDto;
import account.mapper.EventMapper;
import account.service.event.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class EventController {

    private final EventServiceImpl eventService;
    private final EventMapper mapper;

    @Autowired
    public EventController(EventServiceImpl eventService, EventMapper mapper) {
        this.eventService = eventService;
        this.mapper = mapper;
    }

    @GetMapping("/security/events")
    public ResponseEntity<List<EventDto>> getEvents() {
        return ResponseEntity.ok(mapper.toList(eventService.findAll()));
    }
}

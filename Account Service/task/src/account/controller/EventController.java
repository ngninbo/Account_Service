package account.controller;

import account.domain.AccountServiceCustomErrorMessage;
import account.domain.EventDto;
import account.mapper.EventMapper;
import account.service.event.EventServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auditor service", description = "Event monitoring")
public class EventController {

    private final EventServiceImpl eventService;
    private final EventMapper mapper;

    @Autowired
    public EventController(EventServiceImpl eventService, EventMapper mapper) {
        this.eventService = eventService;
        this.mapper = mapper;
    }

    @GetMapping("/security/events")
    @Operation(description = "Get events history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "401",
                    description = "Access denied",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountServiceCustomErrorMessage.class)) }),
    })
    public ResponseEntity<List<EventDto>> getEvents() {
        return ResponseEntity.ok(mapper.toList(eventService.findAll()));
    }
}

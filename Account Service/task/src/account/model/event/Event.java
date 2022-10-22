package account.model.event;

import account.util.LogEvent;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "event_sequence", sequenceName = "EventSeq")
@Table(name = "log_events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "event_sequence")
    private Long id;
    private String date;
    @Enumerated(EnumType.STRING)
    private LogEvent action;
    private String subject;
    private String object;
    private String path;

    public Event(LogEvent action, String subject, String object, String path) {
        this.date = LocalDateTime.now().toString();
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }
}

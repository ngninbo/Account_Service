package account.domain;

import account.util.LogEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    private LogEvent action;
    private String subject;
    private String object;
    private String path;
}

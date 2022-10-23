package account.repository;

import account.model.event.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {

    List<Event> findAll();
}

package rocketseat.com.passinserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rocketseat.com.passinserver.domain.event.Event;

public interface EventRepository extends JpaRepository<Event, String> {
}

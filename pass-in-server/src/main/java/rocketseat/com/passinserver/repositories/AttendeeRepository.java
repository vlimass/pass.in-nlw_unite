package rocketseat.com.passinserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rocketseat.com.passinserver.domain.attendee.Attendee;

import java.util.List;
import java.util.Optional;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {
    List<Attendee> findByEventId(String eventId);

    Optional<Attendee> findByEventIdAndEmail(String eventId, String email);
}

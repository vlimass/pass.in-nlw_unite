package rocketseat.com.passinserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rocketseat.com.passinserver.domain.attendee.Attendee;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {
}

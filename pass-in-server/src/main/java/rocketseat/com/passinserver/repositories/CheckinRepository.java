package rocketseat.com.passinserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rocketseat.com.passinserver.domain.checkin.CheckIn;

import java.util.Optional;

public interface CheckinRepository extends JpaRepository<CheckIn, Integer> {
    Optional<CheckIn> findByAttendeeId(String attendeeId);
}

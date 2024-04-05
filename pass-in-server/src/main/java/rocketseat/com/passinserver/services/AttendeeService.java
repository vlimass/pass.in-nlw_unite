package rocketseat.com.passinserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rocketseat.com.passinserver.domain.attendee.Attendee;
import rocketseat.com.passinserver.domain.attendee.exceptions.AttendeeAlreadyExistsException;
import rocketseat.com.passinserver.domain.attendee.exceptions.AttendeeNotFoundException;
import rocketseat.com.passinserver.domain.checkin.CheckIn;
import rocketseat.com.passinserver.dto.attendee.AttendeeBadgeResponseDTO;
import rocketseat.com.passinserver.dto.attendee.AttendeeDetails;
import rocketseat.com.passinserver.dto.attendee.AttendeesListResponseDTO;
import rocketseat.com.passinserver.dto.attendee.AttendeeBadgeDTO;
import rocketseat.com.passinserver.repositories.AttendeeRepository;
import rocketseat.com.passinserver.repositories.CheckinRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId) {
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);

            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
    }

    public void verifyAttendeeSubscription(String email, String eventId) {
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);

        if(isAttendeeRegistered.isPresent()) throw new AttendeeAlreadyExistsException("Attendee is already registered.");
    }

    public Attendee registerAttendee(Attendee newAttendee) {
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
    }

    public void checkInAttendee(String attendeeId) {
        Attendee attendee = this.getAttendee(attendeeId);
        this.checkInService.registerCheckIn(attendee);
    }

    private Attendee getAttendee(String attendeeId) {
        return this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID: " + attendeeId));
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        Attendee attendee = this.getAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

        AttendeeBadgeDTO badgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());
        return new AttendeeBadgeResponseDTO(badgeDTO);
    }
}

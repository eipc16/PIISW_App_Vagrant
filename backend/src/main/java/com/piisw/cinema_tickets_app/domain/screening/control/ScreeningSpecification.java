package com.piisw.cinema_tickets_app.domain.screening.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject_;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class ScreeningSpecification extends AuditedObjectSpecification<Screening> {

    public Specification<Screening> whereScreeningRoomIdEqualsAndObjectStateEquals(Long screeningRoomId, ObjectState objectState) {
        return whereScreeningRoomIdEquals(screeningRoomId).and(whereObjectStateEquals(objectState));
    }

    private Specification<Screening> whereScreeningRoomIdEquals(Long screeningRoomId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(Screening_.SCREENING_ROOM).get(AuditedObject_.ID), screeningRoomId);
    }

    public Specification<Screening> whereMovieIdEqualsAndObjectStateIn(Long movieId, Set<ObjectState> objectStates) {
        return whereMovieIdEquals(movieId).and(whereObjectStateIn(objectStates));
    }

    public Specification<Screening> whereMovieIdEquals(Long movieId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(Screening_.MOVIE).get(AuditedObject_.ID), movieId);
    }

    public Specification<Screening> whereStartTimeBetween(LocalDateTime begin, LocalDateTime end) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get(Screening_.START_TIME), begin, end);
    }

}
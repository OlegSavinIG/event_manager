package ru.practicum.explorewithme.event.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventStatus;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Specifications for querying EventEntity objects.
 */
public class EventSpecification {
    protected EventSpecification() {
    }

    /**
     * Creates a specification for filtering events by user IDs.
     *
     * @param users the list of user IDs
     * @return the specification
     */
    public static Specification<EventEntity> hasUsers(
            final List<Long> users) {
        return (root, query, criteriaBuilder) ->
                root.get("initiator").get("id").in(users);
    }

    /**
     * Creates a specification for filtering events by state.
     *
     * @param states the list of states
     * @return the specification
     */
    public static Specification<EventEntity> hasStates(
            final List<EventStatus> states) {
        return (root, query, criteriaBuilder) ->
                root.get("state").in(states);
    }

    /**
     * Creates a specification for filtering events by category IDs.
     *
     * @param categories the list of category IDs
     * @return the specification
     */
    public static Specification<EventEntity> hasCategories(
            final List<Integer> categories) {
        return (root, query, criteriaBuilder) ->
                root.get("category").get("id").in(categories);
    }

    /**
     * Creates a specification for filtering events after a date.
     *
     * @param rangeStart the start date
     * @return the specification
     */
    public static Specification<EventEntity> dateAfter(
            final LocalDateTime rangeStart) {
        return (root, query, criteriaBuilder) -> {
            if (rangeStart == null) {
                return criteriaBuilder.greaterThanOrEqualTo(
                        root.get("eventDate"), LocalDateTime.now());
            }
            return criteriaBuilder.greaterThanOrEqualTo(root
                    .get("eventDate"), rangeStart);
        };
    }

    /**
     * Creates a specification for filtering events before a date.
     *
     * @param rangeEnd the end date
     * @return the specification
     */
    public static Specification<EventEntity> dateBefore(
            final LocalDateTime rangeEnd) {
        return (root, query, criteriaBuilder) -> {
            if (rangeEnd == null) {
                return criteriaBuilder.lessThanOrEqualTo(
                        root.get("eventDate"), LocalDateTime.now());
            }
            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("eventDate"), rangeEnd);
        };
    }

    /**
     * Creates a specification for filtering events containing text.
     *
     * @param text the text to search for
     * @return the specification
     */
    public static Specification<EventEntity> containsText(final String text) {
        return (root, query, criteriaBuilder) -> {
            String pattern = "%" + text.toLowerCase() + "%";
            Predicate annotationPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("annotation")), pattern);
            Predicate descriptionPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")), pattern);
            return criteriaBuilder.or(annotationPredicate,
                    descriptionPredicate);
        };
    }

    /**
     * Creates a specification for filtering events by availability.
     *
     * @return the specification
     */
    public static Specification<EventEntity> isAvailable() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("confirmedRequests"),
                        root.get("participantLimit"));
    }

    /**
     * Creates a specification for filtering events by payment status.
     *
     * @param paid the paid status
     * @return the specification
     */
    public static Specification<EventEntity> isPaid(
            final Boolean paid) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("paid"), paid);
    }

    /**
     * Creates a specification for excluding events with statuses.
     *
     * @param statuses the statuses to exclude
     * @return the specification
     */
    public static Specification<EventEntity> excludeStatuses(
            final EventStatus... statuses) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<EventStatus> inClause =
                    criteriaBuilder.in(root.get("state"));
            for (EventStatus status : statuses) {
                inClause.value(status);
            }
            return criteriaBuilder.not(inClause);
        };

    }
}

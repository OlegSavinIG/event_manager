package ru.practicum.explorewithme.event.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventStatus;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.List;

public class EventSpecification {
    public static Specification<EventEntity> hasUsers(List<Long> users) {
        return (root, query, criteriaBuilder) ->
                root.get("initiator").get("id").in(users);
    }

    public static Specification<EventEntity> hasStates(List<String> states) {
        return (root, query, criteriaBuilder) -> root.get("state").in(states);
    }

    public static Specification<EventEntity> hasCategories(List<Integer> categories) {
        return (root, query, criteriaBuilder) -> root.get("category").get("id").in(categories);
    }

    public static Specification<EventEntity> dateAfter(LocalDateTime rangeStart) {
        return (root, query, criteriaBuilder) -> {
            if (rangeStart == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), LocalDateTime.now());
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart);
        };
    }

    public static Specification<EventEntity> dateBefore(LocalDateTime rangeEnd) {
        return (root, query, criteriaBuilder) -> {
            if (rangeEnd == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), LocalDateTime.now());
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd);
        };
    }

    public static Specification<EventEntity> containsText(String text) {
        return (root, query, criteriaBuilder) -> {
            String pattern = "%" + text.toLowerCase() + "%";
            Predicate annotationPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), pattern);
            Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern);
            return criteriaBuilder.or(annotationPredicate, descriptionPredicate);
        };
    }

    public static Specification<EventEntity> isAvailable() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("confirmedRequests"), root.get("participantLimit"));
    }

    public static Specification<EventEntity> isPaid(Boolean paid) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paid"), paid);
    }

    public static Specification<EventEntity> excludeStatuses(EventStatus... statuses) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<EventStatus> inClause = criteriaBuilder.in(root.get("state"));
            for (EventStatus status : statuses) {
                inClause.value(status);
            }
            return criteriaBuilder.not(inClause);
        };
    }

}

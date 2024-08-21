package ru.practicum.explorewithme.user.request.model;

/**
 * The event is waiting for cancel moderation.
 */
public enum RequestStatus {
    /**
     * The request is pending.
     */
    PENDING,
    /**
     * The request is rejected.
     */
    REJECTED,
    CANCELED,
    /**
     * The request is confirmed.
     */
    CONFIRMED
}

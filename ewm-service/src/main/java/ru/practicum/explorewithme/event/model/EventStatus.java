package ru.practicum.explorewithme.event.model;

/**
 * Enumeration representing the status of an event.
 */
public enum EventStatus {
    /**
     * The event is published and available to the public.
     */
    PUBLISHED,

    /**
     * The event is rejected and not available to the public.
     */
    REJECTED,

    /**
     * The event is approved and awaiting further action.
     */
    PUBLISH_EVENT,

    /**
     * The event is approved and awaiting further action.
     */
    REJECT_EVENT,

    /**
     * The event is waiting for cancel moderation.
     */
    CANCEL_REVIEW,
    /**
     * The event is waiting for cancel moderation.
     */
    SEND_TO_REVIEW,

    /**
     * The event is pending approval or further action.
     */
    PENDING,
    /**
     * The event is canceled.
     */
    CANCELED
}

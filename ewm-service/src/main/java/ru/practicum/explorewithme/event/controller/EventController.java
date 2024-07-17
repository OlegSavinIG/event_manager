package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.client.EventClient;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.event.model.EventResponseShort;
import ru.practicum.explorewithme.event.model.EventSearchCriteria;
import ru.practicum.explorewithme.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService service;
    private final EventClient client;

    @GetMapping
    public ResponseEntity<List<EventResponseShort>> getEvents(
            @ModelAttribute EventSearchCriteria criteria,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest servletRequest
    ) {
        String remoteAddr = servletRequest.getRemoteAddr();
        String requestURI = servletRequest.getRequestURI();
        client.sendRequestData(remoteAddr, requestURI);
        return ResponseEntity.ok(service.getEvents(criteria, from, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEvent(
            @PathVariable Long id,
            HttpServletRequest servletRequest
    ) {
        String remoteAddr = servletRequest.getRemoteAddr();
        String requestURI = servletRequest.getRequestURI();
        client.sendRequestData(remoteAddr, requestURI);
        return ResponseEntity.ok(service.getEvent(id));
    }
}

package ru.practicum.explorewithme.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.StatisticRequest;
import ru.practicum.explorewithme.StatisticResponse;
import ru.practicum.explorewithme.exception.WrongTimeException;
import ru.practicum.explorewithme.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing statistics.
 */
@RestController
@RequiredArgsConstructor
@Validated
public class StatisticController {
    /**
     * REST service for managing statistics.
     */
    private final StatisticService service;

    /**
     * Saves statistical data.
     *
     * @param request the statistic request containing data to be saved
     * @return HttpStatus
     */
    @PostMapping("/hit")
    public ResponseEntity<Void> saveStatistic(@Valid @RequestBody
                                  final StatisticRequest request) {
        service.saveStatistic(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Retrieves statistics based on query parameters.
     *
     * @param start  the start date and time for the statistics
     * @param end    the end date and time for the statistics
     * @param uris   the list of URIs to filter the statistics
     * @param unique whether to consider only unique hits
     * @return the list of statistic responses
     */
    @GetMapping("/stats")
    public List<StatisticResponse> getStatistic(
            @NotNull @RequestParam("start")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            final LocalDateTime start,
            @NotNull @RequestParam("end")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            final LocalDateTime end,
            @RequestParam(value = "uris", required = false)
            final List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false")
            final boolean unique) {
        if (!start.isBefore(end)) {
            throw new WrongTimeException(
                    "Start time must be before end time");
        }
        return service.getStatistic(start, end, uris, unique);
    }

/**
 * Retrieves statistics based on query parameters.
 *
 * @param uris   the list of URIs to filter the statistics
 * @return map event views
 */
    @GetMapping("/views")
    public Map<Long, Long> getEventViews(
            @NotNull @RequestParam("uris")final List<String> uris
    ) {
       return service.getEventViews(uris);
    }
}

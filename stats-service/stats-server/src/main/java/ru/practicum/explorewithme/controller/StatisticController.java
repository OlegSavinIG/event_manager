package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

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
     */
    @PostMapping("/hit")
    public void saveStatistic(@Valid @RequestBody
                                  final StatisticRequest request) {
        service.saveStatistic(request);
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
}

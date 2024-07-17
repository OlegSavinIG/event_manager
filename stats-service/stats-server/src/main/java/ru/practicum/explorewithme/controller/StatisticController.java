package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.StatisticRequest;
import ru.practicum.explorewithme.StatisticResponse;
import ru.practicum.explorewithme.exception.WrongTimeException;
import ru.practicum.explorewithme.service.StatisticService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class StatisticController {
    private final StatisticService service;

    @PostMapping("/hit")
    public void saveStatistic(@Valid @RequestBody final StatisticRequest request) {
        service.saveStatistic(request);
    }

    @GetMapping("/stats")
    public List<StatisticResponse> getStatistic(
            @NotNull @RequestParam("start")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") final LocalDateTime start,
            @NotNull @RequestParam("end")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") final LocalDateTime end,
            @RequestParam(value = "uris", required = false) final List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false") final boolean unique) {
        if (!start.isBefore(end)) {
            throw new WrongTimeException("Начало эвента должно быть раньше конца");
        }
        return service.getStatistic(start, end, uris, unique);
    }
}

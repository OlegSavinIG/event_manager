package ru.practicum.explorewithme.warmup;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.category.service.CategoryService;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.user.model.EventSearchCriteriaForAdmin;
import ru.practicum.explorewithme.user.model.UserRequest;
import ru.practicum.explorewithme.user.repository.AdminEventRepository;
import ru.practicum.explorewithme.user.repository.AdminUserRepository;
import ru.practicum.explorewithme.user.service.admin.AdminEventService;
import ru.practicum.explorewithme.user.service.admin.AdminEventServiceImpl;
import ru.practicum.explorewithme.user.service.admin.AdminUserService;
import ru.practicum.explorewithme.user.service.privateuser.PrivateUserEventsService;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarmUpService {
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final AdminUserRepository adminUserRepository;
private final AdminEventRepository adminEventRepository;
private final AdminEventService adminEventService;
private final AdminUserService adminUserService;
private final PrivateUserEventsService privateUserEventsService;
    private final CategoryRepository categoryRepository;
    @PostConstruct
    public void warmUp() {
        eventRepository.findAll();
        adminUserRepository.findAll();
        categoryRepository.findAll();
        adminEventRepository.findAll();
        adminEventService.warmUp();
        adminUserService.addNewUser(new UserRequest("Ivan", "test@mail.com"));
        eventService.getEventEntitiesForCache();
        log.info("Warmed up");
    }
}

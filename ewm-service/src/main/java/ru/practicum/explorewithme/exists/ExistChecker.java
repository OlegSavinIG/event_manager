package ru.practicum.explorewithme.exists;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.NotExistException;
import ru.practicum.explorewithme.user.repository.AdminUserRepository;
import ru.practicum.explorewithme.user.repository.RequestRepository;

/**
 * Component for checking the existence of various entities.
 */
@RequiredArgsConstructor
@Component
public class ExistChecker {

    private final AdminUserRepository adminUserRepository;
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    /**
     * Checks if a user exists.
     *
     * @param userId the ID of the user
     * @throws NotExistException if the user does not exist
     */
    public void isUserExist(Long userId) {
        boolean existsById = adminUserRepository.existsById(userId);
        if (!existsById) {
            throw new NotExistException("User not exists");
        }
    }

    /**
     * Checks if an event exists.
     *
     * @param eventId the ID of the event
     * @throws NotExistException if the event does not exist
     */
    public void isEventExists(Long eventId) {
        boolean existsById = eventRepository.existsById(eventId);
        if (!existsById) {
            throw new NotExistException("Event not exists");
        }
    }

    /**
     * Checks if a compilation exists.
     *
     * @param compId the ID of the compilation
     * @throws NotExistException if the compilation does not exist
     */
    public void isCompilationExists(Integer compId) {
        boolean existsById = compilationRepository.existsById(compId);
        if (!existsById) {
            throw new NotExistException("Compilation not exists");
        }
    }

    /**
     * Checks if a category exists.
     *
     * @param catId the ID of the category
     * @throws NotExistException if the category does not exist
     */
    public void isCategoryExists(Integer catId) {
        boolean existsById = categoryRepository.existsById(catId);
        if (!existsById) {
            throw new NotExistException("Category not exists");
        }
    }

    /**
     * Checks if a request exists.
     *
     * @param reqId the ID of the request
     * @throws NotExistException if the request does not exist
     */
    public void isRequestExists(Long reqId) {
        boolean existsById = requestRepository.existsById(reqId);
        if (!existsById) {
            throw new NotExistException("Request not exists");
        }
    }
}

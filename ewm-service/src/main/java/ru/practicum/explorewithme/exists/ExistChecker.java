package ru.practicum.explorewithme.exists;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.NotExistException;
import ru.practicum.explorewithme.user.repository.AdminUserRepository;
import ru.practicum.explorewithme.user.repository.RequestRepository;

@RequiredArgsConstructor
@Component
public class ExistChecker {
    private final AdminUserRepository adminUserRepository;
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    public void isUserExist(Long userId) {
        boolean existsById = adminUserRepository.existsById(userId);
        if (!existsById) {
            throw new NotExistException("User not exists");
        }
    }

    public void isEventExists(Long eventId) {
        boolean existsById = eventRepository.existsById(eventId);
        if (!existsById) {
            throw new NotExistException("Event not exists");
        }
    }

    public void isCompilationExists(Integer compId) {
        boolean existsById = compilationRepository.existsById(compId);
        if (!existsById) {
            throw new NotExistException("Compilation not exists");
        }
    }

    public void isCategoryExists(Integer catId) {
        boolean existsById = categoryRepository.existsById(catId);
        if (!existsById) {
            throw new NotExistException("Category not exists");
        }
    }

    public void isRequestExists(Long reqId) {
        boolean existsById = requestRepository.existsById(reqId);
        if (!existsById) {
            throw new NotExistException("Request not exists");
        }
    }
}

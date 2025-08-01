package tech.sanak.deck.service;

import org.springframework.stereotype.Service;
import tech.sanak.deck.model.Flashcard;
import tech.sanak.deck.model.User;
import tech.sanak.deck.repository.FlashcardRepository;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service    // Spring: ta klasa jest logiką biznesową aplikacji
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final UserService userService;

    public FlashcardService(FlashcardRepository flashcardRepository, UserService userService) {
        this.flashcardRepository = flashcardRepository;
        this.userService = userService;
    }

    public List<Flashcard> findAll() {
        return flashcardRepository.findAll();
    }

    public Optional<Flashcard> findById(Long id) {
        return flashcardRepository.findById(id);
    }

    public Flashcard save(Flashcard flashcard) {
        return flashcardRepository.save(flashcard);
    }

    public List<Flashcard> findByOwner(String username) {
        User user = userService.findByUsername(username);
        return flashcardRepository.findByOwner(user);
    }

    public Flashcard saveWithCurrentUser(Flashcard flashcard) {
        User user = userService.getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("Not logged in"));
        flashcard.setOwner(user);
        return flashcardRepository.save(flashcard);
    }

    public void saveCategoryIfNotExists(String category) {
        if (category == null || category.trim().isEmpty()) return;

        List<String> existing = flashcardRepository.findAllDistinctCategories(); // lub użyj setu
        if (!existing.contains(category)) {
            // Dodaj "pustą" fiszkę przypisaną do tej kategorii (lub użyj encji Category jeśli istnieje)
            Flashcard dummy = new Flashcard();
            dummy.setFront("Category placeholder");
            dummy.setBack("Auto-generated to register category");
            dummy.setCategory(category);
            dummy.setPublicCard(false);

            // Opcjonalnie: przypisz admina jako ownera
            userService.getCurrentUser().ifPresent(dummy::setOwner);

            flashcardRepository.save(dummy);
        }
    }

    public List<Flashcard> findAllByCurrentUser() {
        User user = userService.getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("Not logged in"));
        return flashcardRepository.findByOwner(user);
    }

    public List<String> findAllDistinctCategories() {
        return flashcardRepository.findAllDistinctCategories();
    }

    public void deleteById(Long id) {
        flashcardRepository.deleteById(id);
    }

    public List<String> findVisibleCategories() {
        User currentUser = userService.getCurrentUser().orElseThrow();
        return flashcardRepository.findAllVisibleCategories(currentUser);
    }

    public List<String> findVisibleCategories(String username) {
        User user = userService.findByUsername(username);
        return flashcardRepository.findAllVisibleCategories(user);
    }

    public List<Flashcard> findForLearning(List<String> categories, boolean includePublic) {
        User user = userService.getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("Not logged in"));

        System.out.println("Zalogowany użytkownik: " + user.getUsername());
        System.out.println("Wybrane kategorie: " + categories);
        System.out.println("Czy uwzględniać publiczne: " + includePublic);

        // Fiszki użytkownika w wybranych kategoriach
        List<Flashcard> result = new ArrayList<>(
                flashcardRepository.findByOwnerAndCategoryIn(user, categories)
        );

        // Publiczne fiszki w tych samych kategoriach
        if (includePublic) {
            result.addAll(flashcardRepository.findPublicByCategories(categories));
        }

        System.out.println("Znaleziono fiszek: " + result.size());
        return result;
    }


}

package tech.sanak.deck.api;

import org.springframework.web.bind.annotation.*;
import tech.sanak.deck.model.Flashcard;
import tech.sanak.deck.service.FlashcardService;
import tech.sanak.deck.model.LearnRequest;

import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardRestController {

    private final FlashcardService service;

    public FlashcardRestController(FlashcardService service) {
        this.service = service;
    }

    @GetMapping
    public List<Flashcard> getAll() {
        return service.findAllByCurrentUser();
    }

    @PostMapping
    public Flashcard add(@RequestBody Flashcard flashcard) {
        return service.saveWithCurrentUser(flashcard);
    }

    @PostMapping("/learn")
    public List<Flashcard> getFlashcardsForLearning(@RequestBody LearnRequest request) {
        return service.findForLearning(request.getCategories(), request.isIncludePublic());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}

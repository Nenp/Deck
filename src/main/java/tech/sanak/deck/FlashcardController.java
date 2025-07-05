//package tech.sanak.deck.flashcard;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//import org.springframework.ui.Model;
//
///**
// * Kontroler REST, zwraca wszystkie fiszki jako JSON
// */
//
//@Controller
//@RequestMapping("/flashcards")
//public class FlashcardController {
//
//    private final FlashcardService service;
//
//    public FlashcardController(FlashcardService service) {
//        this.service = service;
//    }
//
//    @GetMapping
//    @ResponseBody
//    //  Zwraca wszystkie fiszki jako JSON
//    public List<Flashcard> getAllFlashcards() {
//        return service.findAll();
//    }
//
//    @PostMapping
//    //  Dodaje nowa fiszke danych z JSON
//    public Flashcard addFlashcard(@RequestBody Flashcard flashcard) {
//        return service.save(flashcard);
//    }
//
//    @DeleteMapping("/{id}")
//    //  Usuwa fiszke po ID
//    public void deleteFlashcard(@PathVariable Long id) {
//        service.deleteById(id);
//    }
//
//    @GetMapping("/view")
//    public String showFlashcardsPage(Model model) {
//        List<Flashcard> flashcards = service.findAll();
//        model.addAttribute("flashcards", flashcards);
//        model.addAttribute("flashcard", new Flashcard()); // dla formularza
//        return "flashcards";
//    }
//
//    @PostMapping("/view")
//    public String addFlashcardViaForm(@ModelAttribute Flashcard flashcard) {
//        service.save(flashcard);
//        return "redirect:/flashcards/view";
//    }
//
//    @GetMapping("/test")
//    public String test() {
//        return "test";
//    }
//}

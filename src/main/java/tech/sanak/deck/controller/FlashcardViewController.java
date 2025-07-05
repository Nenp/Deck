package tech.sanak.deck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tech.sanak.deck.model.Flashcard;
import tech.sanak.deck.service.FlashcardService;

@Controller
@RequestMapping("/flashcards")
public class FlashcardViewController {

    private final FlashcardService service;

    public FlashcardViewController(FlashcardService service) {
        this.service = service;
    }

    @GetMapping("/view")
    public String showPage(Model model) {
        model.addAttribute("flashcards", service.findAll());
        model.addAttribute("flashcard", new Flashcard());
        return "flashcards";
    }

    @PostMapping("/view")
    public String addFromForm(@ModelAttribute Flashcard flashcard) {
        service.save(flashcard);
        return "redirect:/flashcards/view";
    }
}

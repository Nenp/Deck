package tech.sanak.deck.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.sanak.deck.model.Flashcard;
import tech.sanak.deck.model.User;
import tech.sanak.deck.service.FlashcardService;
import tech.sanak.deck.service.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/flashcards")
public class FlashcardController {

    private final FlashcardService flashcardService;
    private final UserService userService;

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("flashcard", new Flashcard());
        return "add-card";
    }

    @PostMapping("/add")
    public String handleAddFlashcard(@ModelAttribute Flashcard flashcard,
                                     @RequestParam(required = false) Long userId,
                                     Principal principal) {

        // Get logged-in user
        User owner = userService.findByUsername(principal.getName());

        // If admin and userId provided, assign flashcard to that user
        if (userId != null && userService.hasAdminRole(principal.getName())) {
            userService.findById(userId).ifPresent(flashcard::setOwner);
        } else {
            flashcard.setOwner(owner);
        }

        flashcardService.save(flashcard);
        return "redirect:/flashcards/view";
    }
}

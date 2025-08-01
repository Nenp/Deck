//package tech.sanak.deck.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import tech.sanak.deck.model.Flashcard;
//import tech.sanak.deck.model.User;
//import tech.sanak.deck.service.FlashcardService;
//import tech.sanak.deck.service.UserService;
//
//import java.security.Principal;
//
//@Controller
//@RequestMapping("/flashcards")
//public class FlashcardViewController {
//
//    private final FlashcardService service;
//    private final UserService userService;
//
//
//    public FlashcardViewController(FlashcardService service) {
//        this.service = service;
//    }
//
//    @GetMapping("/view")
//    public String showPage(Model model) {
//        model.addAttribute("flashcards", service.findAll());
//        model.addAttribute("flashcard", new Flashcard());
//        return "flashcards";
//    }
//
//    @PostMapping("/view")
//    public String addFromForm(@ModelAttribute Flashcard flashcard, Principal principal) {
//        String username = principal.getName();
//        User user = userService.findByUsername(username);
//        flashcard.setOwner(user);
//        service.save(flashcard);
//        return "redirect:/flashcards/view";
//    }
//}
//
//


package tech.sanak.deck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.sanak.deck.model.Flashcard;
import tech.sanak.deck.model.User;
import tech.sanak.deck.service.FlashcardService;
import tech.sanak.deck.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/flashcards")
public class FlashcardViewController {

    private final FlashcardService service;
    private final UserService userService;

    public FlashcardViewController(FlashcardService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping("/view")
    public String showPage(Model model, Principal principal) {
        String username = principal.getName();
        List<Flashcard> flashcards = service.findByOwner(username);
        List<String> categories = service.findVisibleCategories(username);

        model.addAttribute("flashcards", flashcards);
        model.addAttribute("flashcard", new Flashcard());
        model.addAttribute("categories", categories);

        return "flashcards";
    }

    @PostMapping("/view")
    public String addFromForm(@ModelAttribute Flashcard flashcard,
                              @RequestParam(required = false) Long userId,
                              Principal principal) {
        User user;

        if (userId != null && userService.hasAdminRole(principal.getName())) {
            user = userService.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User ID not found: " + userId));
        } else {
            user = userService.findByUsername(principal.getName());
        }

        flashcard.setOwner(user);
        service.save(flashcard);
        return "redirect:/flashcards/view";
    }

}

package tech.sanak.deck.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import tech.sanak.deck.model.LearnRequest;
import tech.sanak.deck.service.FlashcardService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LearnController {

    private final FlashcardService flashcardService;

    @GetMapping("/learn")
    public String showLearnForm(Model model) {
        List<String> userCategories = flashcardService.findUserCategories();
        model.addAttribute("categories", userCategories);
        model.addAttribute("learnRequest", new LearnRequest());
        return "learn";
    }

    @PostMapping("/learn")
    public String processLearnRequest(@ModelAttribute LearnRequest learnRequest, Model model) {
        var flashcards = flashcardService.findForLearning(
                learnRequest.getCategories(),
                learnRequest.isIncludePublic()
        );

        if (flashcards.isEmpty()) {
            List<String> userCategories = flashcardService.findUserCategories();
            model.addAttribute("categories", userCategories);
            model.addAttribute("learnRequest", learnRequest);
            model.addAttribute("noResults", true);
            return "learn"; // back to the same form
        }

        model.addAttribute("flashcards", flashcards);
        return "learn-result";
    }

}

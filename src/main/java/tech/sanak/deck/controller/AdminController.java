package tech.sanak.deck.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.sanak.deck.service.FlashcardService;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final FlashcardService flashcardService;

    @GetMapping("/categories")
    public String showCategories(Model model) {
        List<String> categories = flashcardService.findVisibleCategories();
        model.addAttribute("categories", categories);
        return "admin/categories";
    }


    @PostMapping("categories")
    public String addCategory(@RequestParam String newCategory) {
        flashcardService.saveCategoryIfNotExists(newCategory);
        return "redirect:/admin/categories";
    }

}

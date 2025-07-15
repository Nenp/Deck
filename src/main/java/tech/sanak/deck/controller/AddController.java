//package tech.sanak.deck.controller;
//
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import tech.sanak.deck.model.Flashcard;
//
//public class AddController {
//
//    @GetMapping("/add")
//    public String showAddForm(Model model) {
//        model.addAttribute("flashcard", new Flashcard());
//        return "add-card";
//    }
//
//
//    @PostMapping("/add")
//    public String submitAddForm(@ModelAttribute Flashcard flashcard) {
//
//
//    }
//
//}

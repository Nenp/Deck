package tech.sanak.deck.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.sanak.deck.model.Flashcard;
import tech.sanak.deck.model.QuizRequest;
import tech.sanak.deck.service.FlashcardService;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import tech.sanak.deck.model.QuizSession;


@Controller
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final FlashcardService flashcardService;

    @GetMapping
    public String showQuizForm(Model model) {
        model.addAttribute("quizRequest", new QuizRequest());
        model.addAttribute("categories", flashcardService.findVisibleCategories());
        return "quiz";
    }

    @PostMapping("/start")
    public String startQuiz(@ModelAttribute QuizRequest quizRequest, HttpSession session, Model model) {
        List<String> categories = quizRequest.getCategories();

        // Ensure at least one category is slected (public flashcards r not sufficient)
        if (categories == null || categories.isEmpty()) {
            model.addAttribute("quizRequest", quizRequest);
            model.addAttribute("categories", flashcardService.findVisibleCategories());
            model.addAttribute("quizError", "Musisz wybrać przynajmniej jedną kategorię.");

            return "quiz";  // Return to the quiz form with error
        }

        // Resource-aware development:
        // Limit number of categories to avoid heavy DB queries in large-scale use
        // for scalability and performance
        if (categories.size() > 5) {
            model.addAttribute("quizRequest", quizRequest);
            model.addAttribute("categories", flashcardService.findVisibleCategories());
            model.addAttribute("quizError", "you can select up to 5 categories.");
            return "quiz";
        }

        // Fetch flashcards based on selected categories n public flag.
        List<Flashcard> flashcards = flashcardService.findForLearning(
                categories,
                quizRequest.isIncludePublic()
        );
        Collections.shuffle(flashcards);  // Shuffle for Randomized flashcard order

        // Handle case where no flashcards match the filter
        if (flashcards.isEmpty()) {
            model.addAttribute("quizRequest", quizRequest);
            model.addAttribute("categories", flashcardService.findVisibleCategories());
            model.addAttribute("quizError", "Brak fiszek spełniających kryteria.");
            return "quiz";
        }

        // Store quiz state in session
        session.setAttribute("quizQueue", new LinkedList<>(flashcards));
        session.setAttribute("quiz", new QuizSession(flashcards));

        return "redirect:/quiz/next";
    }

    /**
     *  Displays next question in the current quiz session.
     */
    @GetMapping("/next")
    public String showNext(Model model, HttpSession session) {
        LinkedList<Flashcard> queue = (LinkedList<Flashcard>) session.getAttribute("quizQueue");

        // If queue is empty or missing end the quiz
        if (queue == null || queue.isEmpty()) {
            return "quiz-finished";
        }

        Flashcard current = queue.poll(); // Fetch then remove current question from queue
                                          // poll() returns null if queue empty
        session.setAttribute("quizQueue", queue);  // Save updated queue
        model.addAttribute("card", current);  // Show current card

        // Pass full quiz session object to the view for progress display
        QuizSession quizSession = (QuizSession) session.getAttribute("quiz");
        model.addAttribute("quiz", quizSession);


        return "quiz-question";
    }


    /**
     *  Handles answer submission and updates quiz progress.
     */
    @PostMapping("/answer")
    public String handleAnswer(@RequestParam boolean isCorrect, HttpSession session) {

        QuizSession quiz = (QuizSession) session.getAttribute("quiz");


        if (isCorrect) quiz.incrementCorrect();  // Count corret answer

        quiz.next();  // Advance to next question

        if (quiz.isFinished()) {
            return "redirect:/quiz/finished";
        }
        return "redirect:/quiz/next";
    }


    /**
     *  Displays the quiz results and clears session state.
     */
    @GetMapping("/finished")
    public String showQuizFinished(HttpSession session, Model model) {
        QuizSession quiz = (QuizSession) session.getAttribute("quiz");

        if (quiz == null) {
            return "redirect:/quiz";
        }

        model.addAttribute("correctCount", quiz.getCorrectCount());
        model.addAttribute("total", quiz.getFlashcards().size());

        // Clean up session atributes
        session.removeAttribute("quiz");
        session.removeAttribute("quizQueue");

        return "quiz-finished";
    }
}

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
        model.addAttribute("categories", flashcardService.findUserCategories());
        return "quiz";
    }

    @PostMapping("/start")
    public String startQuiz(@ModelAttribute QuizRequest quizRequest, HttpSession session, Model model) {
        List<String> categories = quizRequest.getCategories();

        // jeśli pusto i nie zaznaczono includePublic – błąd
        if ((categories == null || categories.isEmpty()) && !quizRequest.isIncludePublic()) {
            model.addAttribute("quizRequest", quizRequest);
            model.addAttribute("categories", flashcardService.findUserCategories());
            model.addAttribute("noSelection", true);  // flaga do wyświetlenia komunikatu
            return "quiz";  // wraca na formularz z info
        }

        List<Flashcard> flashcards = flashcardService.findForLearning(
                categories,
                quizRequest.isIncludePublic()
        );
        Collections.shuffle(flashcards);

        session.setAttribute("quizQueue", new LinkedList<>(flashcards));
        session.setAttribute("quiz", new QuizSession(flashcards));

        return "redirect:/quiz/next";
    }


    @GetMapping("/next")
    public String showNext(Model model, HttpSession session) {
        LinkedList<Flashcard> queue = (LinkedList<Flashcard>) session.getAttribute("quizQueue");
        if (queue == null || queue.isEmpty()) {
            return "quiz-finished";
        }

        Flashcard current = queue.poll(); // pobierz i usuń
        session.setAttribute("quizQueue", queue);
        model.addAttribute("card", current);
        return "quiz-question";
    }

    @PostMapping("/answer")
    public String handleAnswer(@RequestParam boolean isCorrect, HttpSession session) {

        QuizSession quiz = (QuizSession) session.getAttribute("quiz");

        if (isCorrect) quiz.incrementCorrect();
        quiz.next();

        if (quiz.isFinished()) {
            return "redirect:/quiz/finished";
        }
        return "redirect:/quiz/next";
    }

    @GetMapping("/finished")
    public String showQuizFinished(HttpSession session, Model model) {
        QuizSession quiz = (QuizSession) session.getAttribute("quiz");

        if (quiz == null) {
            return "redirect:/quiz";
        }

        model.addAttribute("correctCount", quiz.getCorrectCount());
        model.addAttribute("total", quiz.getFlashcards().size());

        session.removeAttribute("quiz"); // wyczysc quiz z sesji po zakończeniu
        session.removeAttribute("quizQueue");

        return "quiz-finished";
    }

}

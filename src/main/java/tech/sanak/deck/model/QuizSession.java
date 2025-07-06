package tech.sanak.deck.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class QuizSession {
    private List<Flashcard> flashcards;
    private int index = 0;
    @Getter
    private int correctCount = 0;

    public QuizSession(List<Flashcard> flashcards) {
        this.flashcards = flashcards != null ? flashcards : Collections.emptyList();
    }

    public Flashcard getCurrent() {
        return index < flashcards.size() ? flashcards.get(index) : null;
    }

    public void next() {
        index++;
    }

    public boolean isFinished() {
        return index >= flashcards.size();
    }

    public void incrementCorrect() {
        correctCount++;
    }

}

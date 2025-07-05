package tech.sanak.deck.flashcard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tech.sanak.deck.model.Flashcard;
import tech.sanak.deck.repository.FlashcardRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // uruchamia tylko warstwę JPA z bazą testową
class FlashcardRepositoryTest {

    @Autowired
    private FlashcardRepository repository;

    @Test
    void shouldSaveAndLoadFlashcard() {
        Flashcard flashcard = Flashcard.builder()
                .front("What is the capital of France?")
                .back("Paris")
                .category("Geography")
                .build();

        Flashcard saved = repository.save(flashcard);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFront()).isEqualTo("What is the capital of France?");
        assertThat(repository.findById(saved.getId())).contains(saved);
    }
}

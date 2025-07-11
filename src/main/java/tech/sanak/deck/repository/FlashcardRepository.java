package tech.sanak.deck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.sanak.deck.model.Flashcard;
import tech.sanak.deck.model.User;

import java.util.List;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

    List<Flashcard> findByOwner(User user);
    List<Flashcard> findByOwnerAndCategoryIn(User user, List<String> categories);

    List<Flashcard> findByIsPublicTrueAndCategoryIn(List<String> categories);


    @Query("SELECT DISTINCT f.category FROM Flashcard f WHERE f.owner = :user OR f.isPublic = true")
    List<String> findAllVisibleCategories(@Param("user") User user);

    @Query("SELECT f FROM Flashcard f WHERE f.owner = :user AND f.category IN :categories")
    List<Flashcard> findByOwnerAndCategories(@Param("user") User user, @Param("categories") List<String> categories);

    @Query("SELECT f FROM Flashcard f WHERE f.isPublic = true AND f.category IN :categories")
    List<Flashcard> findPublicByCategories(@Param("categories") List<String> categories);


    @Query("SELECT f FROM Flashcard f WHERE f.category IN :categories AND (f.owner = :user OR f.isPublic = true)")
    List<Flashcard> findForUserWithPublic(@Param("user") User user, @Param("categories") List<String> categories);



}

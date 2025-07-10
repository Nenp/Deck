package tech.sanak.deck.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users") // zmiana nazwy, bo "user" jest słowem kluczowym w wielu DB
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    w razie potrzeby na pozniej
//    @OneToMany(mappedBy = "owner")
//    private List<Flashcard> flashcards;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role; // "USER", "ADMIN"
}

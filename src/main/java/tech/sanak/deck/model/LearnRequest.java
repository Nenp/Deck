package tech.sanak.deck.model;

import lombok.Data;

import java.util.List;

@Data
public class LearnRequest {

    private List<String> categories;
    private boolean includePublic;

}
package com.challenge.explorador_literario.entities;

import com.challenge.explorador_literario.models.AuthorData;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private int birthYear;
    private int deathYear;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Books> books;

    public Author() {}

    public Author(AuthorData authorData) {
        this.name = authorData.name();
        this.birthYear = authorData.birthYear();
        this.deathYear = authorData.deathYear();
    }

    public String getName() {
        return name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public int getDeathYear() {
        return deathYear;
    }

    @Override
    public String toString() {
        return "Author='" + name +
                "\nBirth year=" + birthYear +
                "\nDeath year=" + deathYear +
                "\nBooks=" + books.stream().map(Books::getTitle).collect(Collectors.toList()) +
                "\n-------------------------";
    }
}
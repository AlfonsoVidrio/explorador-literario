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
    private int BirthYear;
    private int DeathYear;
    @OneToMany (mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Books> books;

    public Author() {}

    public Author(AuthorData authorData) {
        this.name = authorData.name();
        this.BirthYear = authorData.birthYear();
        this.DeathYear = authorData.deathYear();
    }

    public String getName() {
        return name;
    }

    public int getBirthYear() {
        return BirthYear;
    }

    public int getDeathYear() {
        return DeathYear;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "***************" +
                "\n**   AUTHOR   **" +
                "\n***************" +
                "\nAuthor='" + name +
                "\nBirth year=" + BirthYear +
                "\nDeath year=" + DeathYear +
                "\nBooks=" + books.stream().map(Books::getTitle).collect(Collectors.toList());
    }
}

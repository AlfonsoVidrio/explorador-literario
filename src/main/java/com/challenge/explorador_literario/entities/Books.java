package com.challenge.explorador_literario.entities;

import com.challenge.explorador_literario.models.AuthorData;
import com.challenge.explorador_literario.models.BooksData;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "books")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    private String language;
    private Long downloadCount;
    @ManyToOne
    private Author author;

    public Books() {
    }

    public Books(BooksData booksData) {
        this.title = booksData.title();
        this.language = booksData.languages().get(0);
        this.downloadCount = booksData.downloadCount();
        this.author = new Author(booksData.authors().get(0));
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }

    public Long getDownloadCount() {
        return downloadCount;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "***************" +
                "\n**   LIBRO   **" +
                "\n***************" +
                "\nTitle='" + title +
                "\nAuthor=" + author.getName() +
                "\nLanguage=" + language +
                "\nDownload count=" + downloadCount;
    }


}

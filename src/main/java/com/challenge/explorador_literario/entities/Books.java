package com.challenge.explorador_literario.entities;

import com.challenge.explorador_literario.models.BooksData;
import jakarta.persistence.*;


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
        this.language = booksData.languages().isEmpty() ? "Unknown" : booksData.languages().get(0);
        this.downloadCount = booksData.downloadCount();
        this.author = booksData.authors().isEmpty() ? null : new Author(booksData.authors().get(0));
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
        return "Title='" + title +
                "\nAuthor=" + (author != null ? author.getName() : "Unknown") +
                "\nLanguage=" + language +
                "\nDownload count=" + downloadCount +
                "\n-------------------------";
    }
}
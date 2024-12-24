package com.challenge.explorador_literario.repository;

import com.challenge.explorador_literario.entities.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {

    @Query("SELECT b FROM Books b WHERE LOWER(b.language) = LOWER(:language)")
    List<Books> booksByLanguage(String language);

    @Query("SELECT b FROM Books b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Books> booksByTitle(String title);

    @Query("SELECT b FROM Books b ORDER BY b.downloadCount DESC LIMIT 10")
    List<Books> top10BooksMostDownloaded();
}
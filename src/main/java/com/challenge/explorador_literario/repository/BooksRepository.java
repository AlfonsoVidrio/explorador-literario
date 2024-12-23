package com.challenge.explorador_literario.repository;

import com.challenge.explorador_literario.entities.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {
    Optional<Books> findByTitle(String title);
}
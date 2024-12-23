package com.challenge.explorador_literario.main;

import com.challenge.explorador_literario.api.ApiClient;
import com.challenge.explorador_literario.entities.Author;
import com.challenge.explorador_literario.entities.Books;
import com.challenge.explorador_literario.models.Data;
import com.challenge.explorador_literario.repository.BooksRepository;
import com.challenge.explorador_literario.repository.AuthorRepository;
import com.challenge.explorador_literario.services.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

@Component
public class Main {
    private Scanner sn = new Scanner(System.in);
    private ApiClient apiClient = new ApiClient();
    private DataConverter dataConverter = new DataConverter();
    private final String BASE_URL = "https://gutendex.com/books/";
    private final BooksRepository booksRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public Main(BooksRepository booksRepository, AuthorRepository authorRepository) {
        this.booksRepository = booksRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() {
        int option = -1;
        while (option != 0) {
            String menu = """
                \n1 - Buscar libros por título
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en determinado año
                5 - Listar libros por idioma
                0 - Salir
                """;
            System.out.println(menu);
            option = sn.nextInt();
            sn.nextLine();

            switch (option) {
                case 1 -> searchBooksByTitle();
                // case 2 -> listBooks();
                // case 3 -> listAuthors();
                // case 4 -> listAuthorsByYear();
                // case 5 -> listBooksByLanguage();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private void searchBooksByTitle() {
        System.out.println("Ingrese el título del libro:");
        String title = sn.nextLine();
        String response = apiClient.getData(BASE_URL + "?search=" + title.replace(" ", "+"));
        Data booksData = dataConverter.getData(response, Data.class);
        Optional<Books> book = booksData.books().stream()
                .filter(b -> b.title().toUpperCase().contains(title.toUpperCase()))
                .findFirst()
                .map(b -> new Books(b));

        book.ifPresentOrElse(this::processBook, () -> System.out.println("Libro no encontrado"));
    }

    private void processBook(Books book) {
        System.out.println("Libro encontrado:");
        System.out.println(book);

        try {
            processAuthor(book);
            booksRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            System.out.println("El libro ya está registrado.");
        }
    }

    private void processAuthor(Books book) {
        Author author = book.getAuthor();
        if (author != null) {
            Optional<Author> existingAuthor = authorRepository.findByName(author.getName());
            if (existingAuthor.isPresent()) {
                book.setAuthor(existingAuthor.get());
            } else {
                authorRepository.save(author);
            }
        }
    }
}
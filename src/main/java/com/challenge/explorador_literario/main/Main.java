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

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Main {
    private final Scanner sn = new Scanner(System.in);
    private final ApiClient apiClient = new ApiClient();
    private final DataConverter dataConverter = new DataConverter();
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
            System.out.print(getMenu());
            option = getIntInput("Entrada inválida. Por favor, ingrese una opción válida.");
            switch (option) {
                case 1 -> registerBookByTitle();
                case 2 -> registerBookByAuthor();
                case 3 -> searchAuthorsByName();
                case 4 -> searchBooksByTitle();
                case 5 -> listEntities(booksRepository.findAll(), "No se encontraron libros registrados.", "Libros encontrados");
                case 6 -> listEntities(authorRepository.findAll(), "No se encontraron autores registrados.", "Autores encontrados");
                case 7 -> listLivingAuthorsByYear();
                case 8 -> listBooksByLanguage();
                case 9 -> top10BooksMostDownloaded();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private String getMenu() {
        return """
                \n1 - Registrar libro por título
                2 - Registrar libro por autor
                3 - Buscar autores por nombre
                4 - Buscar libros por título
                5 - Listar libros registrados
                6 - Listar autores registrados
                7 - Listar autores vivos por año
                8 - Listar libros por idioma
                9 - Top 10 libros más descargados
                0 - Salir
                """;
    }

    private int getIntInput(String errorMessage) {
        try {
            return Integer.parseInt(sn.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(errorMessage);
            return -1;
        }
    }

    private void listEntities(List<?> entities, String emptyMessage, String header) {
        if (entities.isEmpty()) {
            System.out.println(emptyMessage);
        } else {
            System.out.println("----- " + header + " -----");
            entities.forEach(System.out::println);
        }
    }

    private void registerBookByTitle() {
        System.out.println("Ingrese el título del libro:");
        String title = sn.nextLine();
        String response = apiClient.getData(BASE_URL + "?search=" + title.replace(" ", "+"));
        Data booksData = dataConverter.getData(response, Data.class);
        Optional<Books> book = booksData.books().stream()
                .filter(b -> b.title().toUpperCase().contains(title.toUpperCase()))
                .findFirst()
                .map(Books::new);

        book.ifPresentOrElse(this::processBook, () -> System.out.println("Libro no encontrado"));
    }

    private void registerBookByAuthor() {
        System.out.println("Ingrese el nombre del autor:");
        String authorName = sn.nextLine();
        String response = apiClient.getData(BASE_URL + "?search=" + authorName.replace(" ", "+"));
        Data booksData = dataConverter.getData(response, Data.class);

        Optional<Author> firstAuthor = booksData.books().stream()
                .flatMap(b -> b.authors().stream())
                .map(Author::new)
                .filter(a -> a.getName().toLowerCase().contains(authorName.toLowerCase()))
                .findFirst();

        if (firstAuthor.isEmpty()) {
            System.out.println("No se encontraron autores para el nombre especificado.");
        } else {
            Author author = firstAuthor.get();
            List<Books> booksByAuthor = booksData.books().stream()
                    .filter(b -> b.authors().stream().anyMatch(a -> a.name().contains(author.getName())))
                    .map(Books::new)
                    .toList();

            if (booksByAuthor.isEmpty()) {
                System.out.println("No se encontraron libros para el autor especificado.");
            } else {
                System.out.println("El autor encontrado es:");
                System.out.println(author.getName());
                System.out.println("Presione 1 para guardar todos los libros de este autor");
                System.out.println("Presione 2 para buscar un libro específico");
                String choice = sn.nextLine();
                if (choice.equals("1")) {
                    booksByAuthor.forEach(this::processBook);
                } else if (choice.equals("2")) {
                    registerSingleBook(booksByAuthor);
                } else {
                    System.out.println("Elección inválida. Operación cancelada.");
                }
            }
        }
    }

    private void searchAuthorsByName() {
        System.out.println("Ingrese el nombre del autor:");
        String name = sn.nextLine();
        listEntities(authorRepository.authorsByName(name), "No se encontraron autores para el nombre especificado.", "Autores encontrados");
    }

    private void searchBooksByTitle() {
        System.out.println("Ingrese el título del libro:");
        String title = sn.nextLine();
        listEntities(booksRepository.booksByTitle(title), "No se encontraron libros para el título especificado.", "Libros encontrados");
    }

    private void listLivingAuthorsByYear() {
        System.out.println("Ingrese el año:");
        int year = getIntInput("Entrada inválida. Por favor, ingrese un año válido.");
        if (year != -1) {
            listEntities(authorRepository.authorsByYear(year), "No se encontraron autores vivos para el año especificado.", "Autores encontrados");
        }
    }

    private void listBooksByLanguage() {
        System.out.println("Ingrese el idioma (ES, EN, FR):");
        String language = sn.nextLine().toUpperCase();
        if (language.isEmpty() || (!language.equals("ES") && !language.equals("EN") && !language.equals("FR"))) {
            System.out.println("Entrada inválida. Por favor, ingrese un idioma válido (ES, EN, FR).");
            return;
        }
        listEntities(booksRepository.booksByLanguage(language), "No se encontraron libros para el idioma especificado.", "Libros encontrados");
    }

    private void top10BooksMostDownloaded() {
        listEntities(booksRepository.top10BooksMostDownloaded(), "No se encontraron libros más descargados.", "Top 10 libros más descargados");
    }

    private void registerSingleBook(List<Books> booksByAuthor) {
        boolean bookFound = false;
        while (!bookFound) {
            System.out.println("Ingrese el título del libro que desea guardar (o escriba 'cancelar' para salir):");
            String bookTitle = sn.nextLine();
            if (bookTitle.equalsIgnoreCase("cancelar")) {
                System.out.println("Operación cancelada.");
                break;
            }
            Optional<Books> book = booksByAuthor.stream()
                    .filter(b -> b.getTitle().toLowerCase().contains(bookTitle.toLowerCase()))
                    .findFirst();
            if (book.isPresent()) {
                processBook(book.get());
                bookFound = true;
            } else {
                System.out.println("Libro no encontrado. Inténtelo de nuevo.");
            }
        }
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
package com.aluracursos.bookshelf;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.aluracursos.bookshelf.api.AskApi;
import com.aluracursos.bookshelf.api.ConvertData;
import com.aluracursos.bookshelf.models.Author;
import com.aluracursos.bookshelf.models.Books;
import com.aluracursos.bookshelf.models.DTOAuthors;
import com.aluracursos.bookshelf.models.DTOBooks;
import com.aluracursos.bookshelf.models.Data;
import com.aluracursos.bookshelf.repository.AuthorRepository;
import com.aluracursos.bookshelf.repository.BookRepository;

@Service
public class Principal {
    private Scanner scn = new Scanner(System.in);
    private AskApi api = new AskApi();
    private ConvertData data = new ConvertData();
    private final String URL = "https://gutendex.com/books/?search=";

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    public Principal(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public void showMenu() {
        int option = -1;
        while (option != 0) {
            System.out.println("\n=== Bookshelf Menu ===");
            System.out.println("1. Buscar y guardar libro");
            System.out.println("2. Buscar y guardar autor");
            System.out.println("3. Listar libros por idioma");
            System.out.println("4. Listar autores vivos en un año");
            System.out.println("5. Mostrar estadísticas");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                option = Integer.parseInt(scn.nextLine());
                switch (option) {
                    case 1 -> searchBook();
                    case 2 -> searchAuthor();
                    case 3 -> listBooksByLanguage();
                    case 4 -> listAuthorsAliveInYear();
                    case 5 -> showStatistics();
                    case 0 -> System.out.println("¡Hasta luego!");
                    default -> System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido.");
            }
        }
    }

    public void searchBook() {
        System.out.println("Enter name of book: ");
        var name = scn.nextLine();
        var json = api.getData(URL + name.replace(" ", "%20").toLowerCase());
        var data_obtained = data.convertObtainedData(json, Data.class);

        Optional<DTOBooks> found = data_obtained.results()
        .stream()
        .filter(b -> b.title().toLowerCase().contains(name.toLowerCase()))
        .findFirst();

        if (found.isPresent()) {
            DTOBooks books = found.get();
            Books book = new Books(books);

            // Buscar o crear el autor
            if (!books.authors().isEmpty()) {
                DTOAuthors authorDTO = books.authors().get(0);
                Optional<Author> existingAuthor = authorRepository.findByName(authorDTO.name());

                Author author;
                if (existingAuthor.isPresent()) {
                    author = existingAuthor.get();
                } else {
                    author = new Author(authorDTO);
                    authorRepository.save(author);
                }

                book.setAuthor(author);
            }

            bookRepository.save(book);

            // Imprimir información del libro en consola
            System.out.println("\n=== Libro guardado exitosamente ===");
            System.out.println("Título: " + book.getTitle());
            System.out.println("Idioma: " + (book.getLanguage() != null ? book.getLanguage().getIdioma() : "No especificado"));
            System.out.println("Descargas: " + book.getDownloads());
            if (book.getAuthor() != null) {
                System.out.println("Autor: " + book.getAuthor().getName());
            }
            System.out.println("ID en BD: " + book.getId());
        } else {
            System.out.println("No se encontró ningún libro con ese título.");
        }
    }

    public void searchAuthor() {
        System.out.println("Enter name of author: ");
        var name = scn.nextLine();
        var json = api.getData(URL + name.replace(" ", "%20").toLowerCase());
        var data_obtained = data.convertObtainedData(json, Data.class);

        // Buscar libros del autor y extraer información del autor
        Optional<DTOBooks> bookWithAuthor = data_obtained.results()
        .stream()
        .filter(b -> b.authors().stream()
            .anyMatch(a -> a.name().toLowerCase().contains(name.toLowerCase())))
        .findFirst();

        if (bookWithAuthor.isPresent()) {
            DTOAuthors authorDTO = bookWithAuthor.get().authors().get(0);

            // Verificar si el autor ya existe
            Optional<Author> existingAuthor = authorRepository.findByName(authorDTO.name());

            if (existingAuthor.isPresent()) {
                System.out.println("El autor '" + authorDTO.name() + "' ya existe en la base de datos.");
                return;
            }

            // Crear y guardar el autor
            Author author = new Author(authorDTO);
            authorRepository.save(author);

            System.out.println("\n=== Autor guardado exitosamente ===");
            System.out.println("Nombre: " + author.getName());
            System.out.println("Año de nacimiento: " + (author.getBirthYear() != null ? author.getBirthYear() : "No especificado"));
            System.out.println("Año de fallecimiento: " + (author.getDeathYear() != null ? author.getDeathYear() : "No especificado"));
            System.out.println("ID en BD: " + author.getId());
        } else {
            System.out.println("No se encontró ningún autor con ese nombre.");
        }
    }

    public void listBooksByLanguage() {
        System.out.println("Enter language code (es, en, fr, pt): ");
        var language = scn.nextLine().toLowerCase();

        List<Books> books = bookRepository.findBooksByLanguage(language);

        if (books.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma '" + language + "'.");
        } else {
            System.out.println("\n=== Libros en idioma '" + language.toUpperCase() + "' ===");
            books.forEach(book -> {
                System.out.println("Título: " + book.getTitle());
                System.out.println("Autor: " + (book.getAuthor() != null ? book.getAuthor().getName() : "Desconocido"));
                System.out.println("Descargas: " + book.getDownloads());
                System.out.println("---");
            });
            System.out.println("Total de libros: " + books.size());
        }
    }

    public void listAuthorsAliveInYear() {
        System.out.println("Enter year to check living authors: ");
        try {
            int year = Integer.parseInt(scn.nextLine());
            List<Author> authors = authorRepository.findAuthorsAlive(year);

            if (authors.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + year + ".");
            } else {
                System.out.println("\n=== Autores vivos en " + year + " ===");
                authors.forEach(author -> {
                    System.out.println("Nombre: " + author.getName());
                    System.out.println("Año nacimiento: " + (author.getBirthYear() != null ? author.getBirthYear() : "Desconocido"));
                    System.out.println("Año fallecimiento: " + (author.getDeathYear() != null ? author.getDeathYear() : "Desconocido"));
                    System.out.println("Libros: " + author.getBooks().size());
                    System.out.println("---");
                });
                System.out.println("Total de autores: " + authors.size());
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese un año válido (número).");
        }
    }

    public void showStatistics() {
        long totalBooks = bookRepository.count();
        long totalAuthors = authorRepository.count();

        System.out.println("\n=== Estadísticas de Bookshelf ===");
        System.out.println("Total de libros: " + totalBooks);
        System.out.println("Total de autores: " + totalAuthors);

        // Libros más descargados
        List<Books> topBooks = bookRepository.findTop10ByOrderByDownloadsDesc();
        if (!topBooks.isEmpty()) {
            System.out.println("\nTop 10 libros más descargados:");
            topBooks.stream().limit(10).forEach(book ->
                System.out.println("- " + book.getTitle() + " (" + book.getDownloads() + " descargas)")
            );
        }
    }
}

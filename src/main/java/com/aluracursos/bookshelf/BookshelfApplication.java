package com.aluracursos.bookshelf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.aluracursos.bookshelf.repository.AuthorRepository;
import com.aluracursos.bookshelf.repository.BookRepository;

@SpringBootApplication
public class BookshelfApplication implements CommandLineRunner {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private AuthorRepository authorRepository;

	public static void main(String[] args) {
		SpringApplication.run(BookshelfApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("¡Bienvenido a Bookshelf - Tu biblioteca personal!");

		Principal principal = new Principal(authorRepository, bookRepository);
		principal.showMenu();
	}
}

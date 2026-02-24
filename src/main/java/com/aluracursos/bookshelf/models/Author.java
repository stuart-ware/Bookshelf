package com.aluracursos.bookshelf.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private Integer birthYear;
    private Integer deathYear;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Books> books = new ArrayList<>();

    public Author() {}
    public Author(DTOAuthors authors) {
        this.name = authors.name();
        this.birthYear = authors.birth();
        this.deathYear = authors.death();
    }

    public Integer getBirthYear() {
        return birthYear;
    }
    public List<Books> getBooks() {
        return books;
    }
    public Integer getDeathYear() {
        return deathYear;
    }
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }
    public void setBooks(List<Books> books) {
        this.books = books;
    }
    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Author ---------------\n" +
        "id: " + id + "\n" +
        "name: " + name + "\n" +
        "birth year: " + birthYear + "\n" +
        "death year" + deathYear + "\n";
    }
}

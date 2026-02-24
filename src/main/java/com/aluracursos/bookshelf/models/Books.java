package com.aluracursos.bookshelf.models;

import java.util.stream.Collectors;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "book")
public class Books {
    @Id
    private Long id;
    private String title;
    @Enumerated(EnumType.STRING)
    private Language language;
    private Integer downloads;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public Books() {}
    public Books(DTOBooks books) {
        this.id = books.id();
        this.title = books.title();
        this.language = Language.fromString(books.languages().stream().limit(1).collect(Collectors.joining()))
        ;
        this.downloads = books.downloads();
        
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setLanguage(Language language) {
        this.language = language;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Author getAuthor() {
        return author;
    }
    public Integer getDownloads() {
        return downloads;
    }
    public Long getId() {
        return id;
    }
    public Language getLanguage() {
        return language;
    }
    public String getTitle() {
        return title;
    }
    @Override
    public String toString() {
        return "Book ---------------\n" +
        "id: " + id + "\n" +
        "title: " + title + "\n" +
        "downloads: " + downloads + "\n" +
        "author" + author + "\n";
    }
}

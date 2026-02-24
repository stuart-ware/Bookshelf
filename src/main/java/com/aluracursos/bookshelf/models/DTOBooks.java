package com.aluracursos.bookshelf.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DTOBooks(
    @JsonAlias("id") Long id,
    @JsonAlias("title") String title,
    @JsonAlias("authors") List<DTOAuthors> authors,
    @JsonAlias("languages") List<String> languages,
    @JsonAlias("download_count") Integer downloads
) {

}

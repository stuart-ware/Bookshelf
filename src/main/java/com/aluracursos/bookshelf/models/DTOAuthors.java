package com.aluracursos.bookshelf.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DTOAuthors(
    @JsonAlias("name") String name,
    @JsonAlias("birth_year") Integer birth,
    @JsonAlias("death_year") Integer death
) {
    
}

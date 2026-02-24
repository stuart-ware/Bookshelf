package com.aluracursos.bookshelf.models;

public enum Language {
    ES("es"),
    FR("fr"),
    EN("en"),
    PT("pt");

    private String idioma;

    Language(String idioma) {
        this.idioma = idioma;
    }

    public String getIdioma(){
        return this.idioma;
    }

    public static Language fromString(String text) {
        for (Language idioma : Language.values()) {
            if (idioma.idioma.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}

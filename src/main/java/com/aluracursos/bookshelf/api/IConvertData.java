package com.aluracursos.bookshelf.api;

public interface IConvertData {
    <T> T convertObtainedData(String json, Class<T>  a_class);
}

package com.aluracursos.bookshelf.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertData implements IConvertData {
    private ObjectMapper omapper = new ObjectMapper();

    @Override
    public <T> T convertObtainedData(String json, Class<T> a_class) {
        try {
            return omapper.readValue(json, a_class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

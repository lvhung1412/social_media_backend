package com.example.SocialMedia.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class MapHelper {
    public MapHelper() {
    }

    private static ObjectMapper mapper = null;

    public static Map<String, Object> convertObject(Object object){
        if(mapper == null){
            mapper = new ObjectMapper();
        }

        return mapper.convertValue(object, new TypeReference<Map<String, Object>>() {});
    }
}

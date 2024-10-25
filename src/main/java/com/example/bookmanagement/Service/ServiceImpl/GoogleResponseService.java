package com.example.bookmanagement.Service.ServiceImpl;

import com.example.bookmanagement.DTO.BookDTO;
import com.example.bookmanagement.Utils.Constant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GoogleResponseService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GoogleResponseService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<BookDTO> getBooks(String query, String category) {
        String apiUrl;
        if (category != null && !category.isEmpty()) {
            apiUrl = "https://www.googleapis.com/books/v1/volumes?q=subject:" + category + "&key=" + Constant.API_KEY + "&maxResults=21";
        } else {
            apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=" + Constant.API_KEY + "&maxResults=21";
        }

        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);
        return objectMapper.convertValue(response.get("items"), new TypeReference<List<BookDTO>>() {
        });
    }
    public BookDTO getBookDetails(String bookId) {
        String apiUrl = "https://www.googleapis.com/books/v1/volumes/" + bookId + "?key=" + Constant.API_KEY;
        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);
        return objectMapper.convertValue(response, BookDTO.class);
    }
}

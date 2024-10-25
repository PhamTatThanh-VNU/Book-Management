package com.example.bookmanagement.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VolumeInfo {
    private String title;
    private List<String> authors;
    private ImageLinks imageLinks;
    private String description;
    private String previewLink;
    private List<String> categories;
}

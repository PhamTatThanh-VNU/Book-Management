package com.example.bookmanagement.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageLinks {
    private String thumbnail;
    private String medium;
    private String large;
    private String extraLarge;
}

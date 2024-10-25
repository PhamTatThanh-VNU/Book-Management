package com.example.bookmanagement.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDTO {
    private String id;
    private VolumeInfo volumeInfo;
}

package com.lrcmallbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnBookSale {
    private String image;
    private String name;
    private String author;
    private int sale;
}

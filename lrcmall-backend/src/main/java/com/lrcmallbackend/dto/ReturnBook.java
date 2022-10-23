package com.lrcmallbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReturnBook {
    private int bookId;
    private String name;
    private String author;
    private String image;
    private int price;
    private String isbn;
    private int number;
    private int surplus;
    public ReturnBook(int a, String b, String c, String d, int e, String f, int g, int h){
        bookId = a;
        name = b;
        author = c;
        image = d;
        price = e;
        isbn = f;
        surplus = g;
        number = h;
    }

}

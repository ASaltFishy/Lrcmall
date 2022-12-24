package com.lrcmallbackend.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "bookImage")
@Setter
@Getter
public class BookImage {
    @Id
    private int _id;
    private String url;

    public BookImage(int _id,String url){
        // 踩坑:此处构造函数的参数变量名必须与类中的名字一致
        this._id = _id;
        this.url=url;
    }
}

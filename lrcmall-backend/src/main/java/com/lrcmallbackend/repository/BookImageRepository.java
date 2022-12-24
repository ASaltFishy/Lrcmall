package com.lrcmallbackend.repository;

import com.lrcmallbackend.entity.BookImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookImageRepository extends MongoRepository<BookImage, Integer> {

}

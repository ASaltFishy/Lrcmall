package com.lrcmallbackend.repository;

import com.lrcmallbackend.entity.BookType;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookTypeRepository extends Neo4jRepository<BookType, Long> {
    BookType findByName(String name);

    @Query(value = "MATCH q=(:BookType{name:$tag})-[*2]-() RETURN q")
    List<BookType> searchTag(@Param("tag") String name);
}

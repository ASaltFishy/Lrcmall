package com.lrcmallbackend.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

import java.util.HashSet;
import java.util.Set;

@Node
@Getter
@Setter
public class BookType {
    @Id
    @GeneratedValue
    private Long id;
    @Property(name = "name")
    private String name;

    public BookType(String name){
        this.name = name;
    }

    @Relationship(type = "RELATED",direction = Relationship.Direction.OUTGOING)
    public Set<BookType> relateWith;

    public void relateWith(BookType type){
        if(relateWith==null){
            relateWith = new HashSet<>();
        }
        relateWith.add(type);
    }

    @Relationship(type="CHILDOF",direction = Relationship.Direction.OUTGOING)
    public Set<BookType> childOf;

    public void childOf(BookType type){
        if(childOf==null){
            childOf = new HashSet<>();
        }
        childOf.add(type);
    }
}

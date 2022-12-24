package com.lrcmallbackend.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="BOOKS")
@SolrDocument(solrCoreName = "mycore")
@JsonIgnoreProperties(value={"handler","hibernateLazyInitializer","fieldHandler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bookId")
public class Book {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "bookid")
    @Indexed(searchable = false, name = "id")
    private int bookId;
    @Indexed(searchable = false, name = "name", type = "string")
    private String name;
    @Indexed(searchable = false, name = "author", type = "string")
    private String author;
    private String bookType;
    @Indexed(name = "description", type = "string")
    private String discription;
    @Transient
    private String image;
    private int price;
    private String isbn;
    private int surplus;
    private int showStatus;

    @JsonIgnore
    @OneToMany(mappedBy = "book",cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE},fetch = FetchType.LAZY)
    private List<OrderItem> orderitem;

}

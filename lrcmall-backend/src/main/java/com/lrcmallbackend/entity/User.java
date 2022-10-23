package com.lrcmallbackend.entity;

import com.fasterxml.jackson.annotation.*;
import com.sun.istack.NotNull;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="USERS")
@JsonIgnoreProperties(value={"handler","hibernateLazyInitializer","fieldHandler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
public class User {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "userid")
    private int userId;

    private String name;
    private String password;
    private int type; //0 refers to normal user 1 refers to administrator
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "owner",cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE},fetch = FetchType.LAZY)
    public List<Order> orderCarts = new ArrayList<>();



//    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
//    @JoinTable(name="ACCOUNT_DOCUMENT",joinColumns = @JoinColumn(name = "USER_ID"),
//    inverseJoinColumns = @JoinColumn(name = "DOCUMENT_ID"))
//    public List<Document> getProjects(){return projects;}
//    public void setProjects(List<Document> projects){this.projects = projects;}
}

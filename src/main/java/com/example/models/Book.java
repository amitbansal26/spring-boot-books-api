package com.example.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NotNull(message = "error.title.not_null")
    private String title;

    @ElementCollection
    @Size(min = 1, message = "error.authors.not_empty")
    private Set<String> authors = new HashSet<>();

    public Book(String title) {
        this(title, new HashSet<String>());
    }

    public Book(String title, Set<String> authors) {
        this.title = title;
        this.authors = authors;
    }

    public Book() {}

    public String getTitle() {
        return title;
    }

    public Book setTitle(String value) {
        title = value;
        return this;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public Book setAuthors(Set<String> value) {
        authors = value;
        return this;
    }

    public Long getId() {
        return id;
    }
}

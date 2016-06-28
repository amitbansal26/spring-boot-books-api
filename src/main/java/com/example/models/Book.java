package com.example.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NotNull(message = "error.title.notnull")
    private String title;

    public Book(String title) {
        this.title = title;
    }

    public Book() {}

    public String getTitle() {
        return title;
    }

    public Book setTitle(String value) {
        title = value;
        return this;
    }

    public Long getId() {
        return id;
    }
}

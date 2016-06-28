package com.example.models;

import javax.persistence.*;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

package com.example;

import com.example.BooksApplication;
import com.example.models.Book;
import com.example.repositories.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BooksApplication.class)
@WebAppConfiguration
public class BooksApplicationTests {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private BookRepository bookRepository;

    private static byte[] json(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    private List<Book> allBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    private Book firstBook() {
        return allBooks().get(0);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.bookRepository.deleteAll();
    }

    @Test
    public void testCreateBook() throws Exception {
        this.mockMvc.perform(post("/books")
                .contentType(contentType)
                .content(json(new Book("test book"))))
                .andExpect(content().contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(firstBook().getId().intValue())))
                .andExpect(jsonPath("$.title", is("test book")));
    }

    @Test
    public void testCreateBookWitInvalidInput() throws Exception {
        this.mockMvc.perform(post("/books")
                .contentType(contentType)
                .content(json(new Book())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].error", is("Title is a required field")));
    }

    @Test
    public void testGetBookById() throws Exception {
        Book book = bookRepository.save(new Book("test book"));

        this.mockMvc.perform(get("/books/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(book.getId().intValue())))
                .andExpect(jsonPath("$.title", is(book.getTitle())));
    }

    @Test
    public void testGetBookByIdReturns404WhenBookDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/books/123")).andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book book = bookRepository.save(new Book("test book"));

        this.mockMvc.perform(put("/books/" + book.getId())
                .contentType(contentType)
                .content(json(new Book("updated title"))))
                .andExpect(content().contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(book.getId().intValue())))
                .andExpect(jsonPath("$.title", is("updated title")));
    }

    @Test
    public void testUpdateBookWitInvalidInput() throws Exception {
        Book book = bookRepository.save(new Book("test book"));

        this.mockMvc.perform(put("/books/" + book.getId())
                .contentType(contentType)
                .content(json(new Book())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].error", is("Title is a required field")));
    }

    @Test
    public void testUpdateBookReturns404WhenBookDoesNotExist() throws Exception {
        this.mockMvc.perform(put("/books/123")
                .contentType(contentType)
                .content(json(new Book("updated title"))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteBook() throws Exception {
        Book book = bookRepository.save(new Book("test book"));

        this.mockMvc.perform(delete("/books/" + book.getId()))
                .andExpect(status().isOk());

        assertThat(allBooks(), is(empty()));
    }

    @Test
    public void testDeleteBookReturns404WhenBookDoesNotExist() throws Exception {
        this.mockMvc.perform(delete("/books/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllBooks() throws Exception {
        Book book1 = bookRepository.save(new Book("test book1"));
        Book book2 = bookRepository.save(new Book("test book2"));

        this.mockMvc.perform(get("/books/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].id", is(book1.getId().intValue())))
                .andExpect(jsonPath("$[0].title", is(book1.getTitle())))
                .andExpect(jsonPath("$[1].id", is(book2.getId().intValue())))
                .andExpect(jsonPath("$[1].title", is(book2.getTitle()))) ;
    }

    @Test
    public void testGetAllBooksWhenThereAreNoBooks() throws Exception {
        this.mockMvc.perform(get("/books/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$..*", is(empty())));
    }
}

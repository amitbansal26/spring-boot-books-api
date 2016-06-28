# spring-boot-books-api

Simple REST API service for managing a collection of books.
Built as an excercise for learning Java8 and Spring Boot.

## Running in dev mode

```
gradle bootRun
```

## Running tests

```
gradle test
```


# API

## Adding a book

```
curl -H "Content-Type: application/json" -X POST -d '{"title": "book1", "authors": ["author1"], "description": "book1 description"}' http://localhost:8080/books
```

Will return something like:

```
{
   "id":1,
   "title":"book1",
   "authors":["author1"],
   "description":"book1 description"
}
```

Title and authors are mandatory fields.
In case of validation failure, an HTTP code 400 and a list of errors are returned:

```
{
  "errors":[
    {"error":"Authors can't be empty"},
    {"error":"Title is a required field"}
  ]
}
```

## Getting list of books

```
curl -X GET http://localhost:8080/books
```

Will return something like:

```
[
  {
    "id":1,
      "title":"book1",
      "authors":["author1"],
      "description":"book1 description"
  },
  {
    "id":2,
    "title":"book2",
    "authors":["author2"],
    "description":"book2 description"
  }
]
```

## Getting single book by id

```
curl -X GET http://localhost:8080/books/1
```

Will return something like:

```
{
   "id":1,
   "title":"book1",
   "authors":["author1"],
   "description":"book1 description"
}
```

HTTP code 404 is returned when there is no book with given id.

## Updating a book

```
curl -H "Content-Type: application/json" -X PUT -d '{"title":"updated book1 title", "authors": ["author1"], "description": "updated book1 description"}' http://localhost:8080/books/1
```

Will return something like:

```
{
  "id":1,
  "title":"updated book1 title",
  "authors":["author1"],
  "description":"updated book1 description"
}
```

HTTP code 404 is returned when there is no book with given id.

Title and authors are mandatory fields.
In case of validation failure, an HTTP code 400 and a list of errors are returned.


## Deleting a book

```
curl -X DELETE http://localhost:8080/books/1
```

HTTP code 404 is returned when there is no book with given id.

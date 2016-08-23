package ru.kpfu.itis.model.entity;

import javax.persistence.*;

@Entity
@Table(
        name = "books",
        uniqueConstraints = {@UniqueConstraint(name = "UK_books_name_author", columnNames = {"name","author"})}
)

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "author")
    private String author;

    //Don't forget to create a constructor without params
    public Book() {}

    public Book(String name, String description, String author) {
        this.name = name;
        this.description = description;
        this.author = author;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    //hashcode & equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (!name.equals(book.name)) return false;
        if (!description.equals(book.description)) return false;
        return author.equals(book.author);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + author.hashCode();
        return result;
    }
}

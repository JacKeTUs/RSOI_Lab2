package com.jacketus.RSOI_Lab2.Booksservice.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@Table(name = "BOOKS")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getAuthorid() {
        return authorid;
    }

    public void setAuthorid(Long authorid) {
        this.authorid = authorid;
    }

    @Column(name = "AUTHORID")
    private Long authorid;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "NAME")
    private String name;

    @Column(name = "GENRE")
    private String genre;

    public String getDescription() {
        return description;
    }

    @Column(name = "DESCRIPTION")
    private String description;

    public void setBuy_nums(int buy_nums) {
        this.buy_nums = buy_nums;
    }

    @Column(name = "BUY_NUMS", columnDefinition = "integer default 0")
    private int buy_nums;

    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String a) {
        this.author = a;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String _g) {
        this.genre = _g;
    }

    public int getBuy_nums() {
        return buy_nums;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Book s = (Book) o;

        return new EqualsBuilder()
                .append(id, s.id)
                .append(author, s.author)
                .append(name, s.name)
                .append(genre, s.genre)
                .append(description, s.description)
                .append(buy_nums, s.buy_nums)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(author)
                .append(name)
                .append(genre)
                .append(description)
                .append(buy_nums)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Book {" +
                        "id=" + id + ", " +
                        "author=" + author + ", " +
                        "name='" + name + '\'' + ", " +
                        "genre=" + genre + ", " +
                        "description=" + description + ", " +
                        "buy_nums=" + buy_nums +
                    '}';
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.jacketus.RSOI_Lab2.licensesservice.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@Table(name = "licenses")
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userID")
    private Long userID;

    @Column(name = "bookID")
    private Long bookID;

    public Long getId() {
        return id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getBookID() {
        return bookID;
    }

    public void setBookID(Long bookID) {
        this.bookID = bookID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        License license = (License) o;

        return new EqualsBuilder()
                .append(id, license.id)
                .append(userID, license.userID)
                .append(bookID, license.bookID)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(userID)
                .append(bookID)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "License{" +
                "id=" + id + ", " +
                "userID='" + userID + '\'' + ", " +
                "bookID='" + bookID + '\'' +
                '}';
    }
}

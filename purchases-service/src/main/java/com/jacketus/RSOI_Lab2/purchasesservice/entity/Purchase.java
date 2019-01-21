package com.jacketus.RSOI_Lab2.purchasesservice.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "song_id")
    private Long song_id;

    @Column(name = "rating")
    private int rating;

    public Long getId() {
        return id;
    }

    public Long getUserID() {
        return user_id;
    }

    public void setUserID(Long userID) {
        this.user_id = userID;
    }

    public Long getSongID() {
        return song_id;
    }

    public void setSongID(Long songID) {
        this.song_id = songID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Purchase purchase = (Purchase) o;

        return new EqualsBuilder()
                .append(id, purchase.id)
                .append(song_id, purchase.song_id)
                .append(user_id, purchase.user_id)
                .append(rating, purchase.rating)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(user_id)
                .append(song_id)
                .append(rating)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id + ", " +
                "user_id='" + user_id + '\'' + ", " +
                "song_id='" + song_id + '\'' + ", " +
                "rating='" + rating + '\'' +
                '}';
    }
}

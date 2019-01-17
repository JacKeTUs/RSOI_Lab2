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

    @Column(name = "UserID")
    private Long userID;

    @Column(name = "SongID")
    private Long songID;

    public Long getId() {
        return id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getSongID() {
        return songID;
    }

    public void setSongID(Long songID) {
        this.songID = songID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Purchase purchase = (Purchase) o;

        return new EqualsBuilder()
                .append(songID, purchase.songID)
                .append(userID, purchase.userID)
                .append(id, purchase.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(userID)
                .append(songID)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id + ", " +
                "userID='" + userID + '\'' + ", " +
                "songID='" + songID + '\'' +
                '}';
    }
}

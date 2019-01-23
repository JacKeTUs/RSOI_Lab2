package com.jacketus.RSOI_Lab2.songsservice.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@Table(name = "SONGS")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ARTIST")
    private String artist;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LINK")
    private String link;

    @Column(name = "RATING", columnDefinition = "double precision default 0.0")
    private double rating;

    @Column(name = "RATE_NUMS", columnDefinition = "integer default 0")
    private int rate_nums;

    @Column(name = "BUY_NUMS", columnDefinition = "integer default 0")
    private int buy_nums;

    public Long getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String a) {
        this.artist = a;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String _link) {
        this.link = _link;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = ((this.rating * rate_nums) + rating) / (rate_nums + 1); rate_nums++;
    }

    public int getRateNum() {
        return this.rate_nums;
    }


    public int getBuy_nums() {
        return buy_nums;
    }

    public void setBuy_nums(int buy_nums) {
        this.buy_nums = buy_nums;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Song s = (Song) o;

        return new EqualsBuilder()
                .append(id, s.id)
                .append(artist, s.artist)
                .append(name, s.name)
                .append(link, s.link)
                .append(rating, s.rating)
                .append(rate_nums, s.rate_nums)
                .append(buy_nums, s.buy_nums)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(artist)
                .append(name)
                .append(link)
                .append(rating)
                .append(rate_nums)
                .append(buy_nums)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Song {" +
                        "id=" + id + ", " +
                        "artist=" + artist + ", " +
                        "name='" + name + '\'' + ", " +
                        "link=" + link + ", " +
                        "rating=" + rating + ", " +
                        "rate_nums=" + rate_nums + ", " +
                        "buy_nums=" + buy_nums +
                    '}';
    }
}

package com.jacketus.RSOI_Lab2.usersservice.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LOGIN", unique = true)
    private String login;

    @Column(name = "NAME")
    private String name;

    @Column(name = "BUY_NUM", columnDefinition = "integer default 0")
    private int buy_num;


    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBuy_num() {
        return buy_num;
    }

    public void setBuy_num(int buy_num) {
        this.buy_num = buy_num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder()
                .append(id, user.id)
                .append(login, user.login)
                .append(name, user.name)
                .append(buy_num, user.buy_num)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(login)
                .append(name)
                .append(buy_num)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                    "id=" + id + ", " +
                    "login='" + login + '\'' + ", " +
                    "name='" + name + '\'' + ", " +
                    "buy_num='" + buy_num + '\'' +
                '}';
    }
}

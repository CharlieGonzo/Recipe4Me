package com.example.demo;

import jakarta.persistence.*;

import java.util.ArrayList;


@Entity
@Table(name = "users")
public class User {

    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    String username;

    String password;

    ArrayList<Recipe> favorite;


    // Default constructor
    public User() {
        favorite = new ArrayList<>();
    }

    public User(String username,String password){
        this.username = username;
        this.password = password;
        favorite = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Recipe> getFavorite() {
        return favorite;
    }

    public void setFavorite(ArrayList<Recipe> favorite) {
        this.favorite = favorite;
    }
}

package com.example.top.pages.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name="rate")
public class Rate {

    @Id
    private String id;
    private String comment;

    @ManyToOne()
    @JoinColumn(name = "item")
    @JsonBackReference
    private Items item;

    public Rate(String id, String comment, Items item) {
        this.id = id;
        this.comment = comment;
        this.item = item;
    }

    public Rate() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }
}

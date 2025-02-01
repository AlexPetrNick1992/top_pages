package com.example.top.pages.models;

import jakarta.persistence.*;

@Entity
@Table(name="categories", schema="public")
public class Categories {
    @Id
    private String id;
    private String name;
    private String desc;

    @ManyToOne
    private Pages pages;

    public Categories(String id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public Categories() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

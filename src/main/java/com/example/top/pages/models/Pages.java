package com.example.top.pages.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "pages")
public class Pages {

    @Id
    private String id;

    private String name;
    private String desc;

    @OneToMany(mappedBy="name")
    private Set<Items> items;

    @OneToOne(mappedBy="pages")
    private Categories category;

    public Pages(String id, String name, String desc, Set<Items> items, Categories category) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.items = items;
        this.category = category;
    }

    public Pages() { }

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

    public Set<Items> getItems() {
        return items;
    }

    public void setItems(Set<Items> items) {
        this.items = items;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }
}

package com.example.top.pages.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name="item")
public class Items {
    @Id
    private String id;
    private String name;
    private String desc;

    @OneToMany(mappedBy = "item")
    private Set<Rate> rate;

    public Items(String id, String name, String desc, Set<Rate> rate_attr) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.rate = rate_attr;
    }

    public Items() { }

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

    public Set<Rate> getRate() {
        return rate;
    }

    public void setRate(Set<Rate> rate_attr) {
        this.rate = rate_attr;
    }
}

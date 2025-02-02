package com.example.top.pages.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name="item")
public class Items {

    @Id
    private UUID id;

    @Column(name="name")
    private String name;
    private String description;

    @OneToMany(mappedBy = "item")
    private Set<Rate> rate;

    public Items(UUID id, String name, String desc, Set<Rate> rate, Category category) {
        this.id = id;
        this.name = name;
        this.description = desc;
        this.rate = rate;
        this.category = category;
    }

    @OneToOne
    @JoinColumn(name = "category")
    private Category category;


}

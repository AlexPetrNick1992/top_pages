package com.example.top.pages.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "pages")
public class Pages {

    @Id
    private UUID id;

    private String name;
    private String desc;

    @OneToMany(mappedBy="name")
    private Set<Items> items;

    public Pages(UUID id, String name, String desc, Set<Items> items, Category category) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.items = items;
    }


}

package com.example.top.pages.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="category", schema="public")
public class Category {
    @Id
    private UUID id;

    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    public Category(UUID id) {
        this.id = id;
    }

}

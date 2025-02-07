package com.example.top.pages.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pages")
public class Pages {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
    private String name;
    private String description;

    @ManyToMany
    @JoinTable(
            name = "pages_items",
            joinColumns = @JoinColumn(name = "pages_id"),
            inverseJoinColumns = @JoinColumn(name = "items_id")
    )
    private Collection<Items> items;

    @OneToOne
    @JoinColumn(name = "category")
    private Category category;
}

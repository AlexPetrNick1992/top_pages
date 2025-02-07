package com.example.top.pages.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="item")
@ToString
public class Items {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(name="name")
    private String name;
    private String description;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<Rate> rate;

    public Items(String name, Collection<Category> category) {
        UUID uuid = UUID.randomUUID();
        this.id = UUID.fromString(uuid.toString());
        this.name = name;
        this.category = category;
        this.description = "No description";
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "item_category",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Collection<Category> category;

}

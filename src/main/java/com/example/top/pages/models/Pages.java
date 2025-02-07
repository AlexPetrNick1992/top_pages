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
    @Column(name = "isapproved")
    private boolean isApproved;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "pages_items",
            joinColumns = @JoinColumn(name = "pages_id"),
            inverseJoinColumns = @JoinColumn(name = "items_id")
    )
    private Collection<Items> items;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "category", referencedColumnName = "id")
    private Category category;

    public Pages(Category category, boolean isApproved, String description, String name) {
        UUID uuid = UUID.randomUUID();
        this.id = UUID.fromString(uuid.toString());
        this.category = category;
        this.isApproved = isApproved;
        this.description = description;
        this.name = name;
    }

    public Pages(Category category, boolean isApproved, String name) {
        UUID uuid = UUID.randomUUID();
        this.id = UUID.fromString(uuid.toString());
        this.category = category;
        this.isApproved = isApproved;
        this.name = name;
    }
}

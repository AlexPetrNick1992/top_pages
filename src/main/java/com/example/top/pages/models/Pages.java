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
    private String title;
    @Column(name = "description_pages")
    private String descriptionPages;
    @Column(name = "isapproved")
    private boolean isApproved;

    @Override
    public String toString() {
        return "Pages{" +
                "id=" + id +
                '}';
    }

    @ManyToMany(cascade =
    {
        CascadeType.DETACH,
                CascadeType.MERGE,
                CascadeType.REFRESH,
                CascadeType.PERSIST
    })
    @JoinTable(
            name = "pages_items",
            joinColumns = @JoinColumn(name = "pages_id"),
            inverseJoinColumns = @JoinColumn(name = "items_id")
    )
    private Collection<Items> items;

    @OneToOne @JoinColumn(name = "category")
    private Category category;

    public Pages(Category category, boolean isApproved, String descriptionPages, String title) {
        UUID uuid = UUID.randomUUID();
        this.id = UUID.fromString(uuid.toString());
        this.category = category;
        this.isApproved = isApproved;
        this.descriptionPages = descriptionPages;
        this.title = title;
    }

    public Pages(Category category, boolean isApproved, String title) {
        UUID uuid = UUID.randomUUID();
        this.id = UUID.fromString(uuid.toString());
        this.category = category;
        this.isApproved = isApproved;
        this.title = title;
    }
}

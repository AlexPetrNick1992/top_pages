package com.example.top.pages.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name="category", schema="public")
public class Category {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @JsonProperty("created_at")
    @Column(name = "created_at")
    private String createdAt;
    @JsonIgnore
    @JsonProperty("updated_at")
    @Column(name = "updated_at")
    private String updatedAt;

    @JsonProperty("created_by")
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "category")
    private Pages pages;


    @JsonIgnore
    @ManyToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @JoinTable(
            name = "item_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private Collection<Items> items;

    public Category(String name, String description, User createdBy, String createdAt) {

        UUID uuid = UUID.randomUUID();
        this.id = UUID.fromString(uuid.toString());
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Category(String name, User createdBy, String createdAt) {
        UUID uuid = UUID.randomUUID();
        this.id = UUID.fromString(uuid.toString());
        this.name = name;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Category(UUID id) {
        this.id = id;
    }

}

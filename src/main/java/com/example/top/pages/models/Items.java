package com.example.top.pages.models;

import com.example.top.pages.models.converter.StatusEntityConverter;
import com.example.top.pages.service.enumerate.StatusEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class Items {
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
    @Column(name="name")
    private String name;
    private String description;

    @Convert(converter = StatusEntityConverter.class)
    @Column(name="status")
    private StatusEntity status;

    @JsonIgnore
    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<Rate> rate;
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

    @ManyToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @JoinTable(
            name = "item_category",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> category;

    @JsonIgnore
    @ManyToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @JoinTable(
            name = "pages_items",
            joinColumns = @JoinColumn(name = "items_id"),
            inverseJoinColumns = @JoinColumn(name = "pages_id")
    )
    private Collection<Pages> pages;

    public Items(String name, List<Category> category, List<Pages> pages, StatusEntity status, User createdBy, String createdAt) {
        UUID uuid = UUID.randomUUID();
        this.id = UUID.fromString(uuid.toString());
        this.name = name;
        this.category = category;
        this.description = "No description";
        this.pages = pages;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
    public Items(String name, List<Pages> pages, StatusEntity status, User createdBy, String createdAt) {
        UUID uuid = UUID.randomUUID();
        this.id = UUID.fromString(uuid.toString());
        this.name = name;
        this.description = "No description";
        this.pages = pages;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
    public Items(String name, StatusEntity status, User createdBy, String createdAt) {
        UUID uuid = UUID.randomUUID();
        this.id = UUID.fromString(uuid.toString());
        this.name = name;
        this.description = "No description";
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Items{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

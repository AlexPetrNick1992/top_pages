package com.example.top.pages.models;

import com.example.top.pages.models.converter.StatusEntityConverter;
import com.example.top.pages.service.enumerate.StatusEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="rate")
public class Rate {

    @JsonProperty(value = "created_at")
    @Column(name="created_at")
    private String createdAt;
    @JsonProperty(value = "updated_at")
    @Column(name="updated_at")
    private String updatedAt;
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
    private String comment;

    @Convert(converter = StatusEntityConverter.class)
    @Column(name="status")
    private StatusEntity status;

    @Column(name="ispositive")
    private boolean isPositive;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "item")
    @JsonBackReference
    private Items item;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    public Rate(Items item, User user, String comment, Boolean isPositive, StatusEntity status, Category category, String createdDate) {
        UUID uuid = UUID.randomUUID();
        this.id = UUID.fromString(uuid.toString());
        this.item = item;
        this.comment = comment;
        this.user = user;
        this.isPositive = isPositive;
        this.status = status;
        this.category = category;
        this.createdAt = createdDate;
        this.updatedAt = null;
    }

    public Rate(Items item, UUID id, String comment, StatusEntity status) {
        this.id = id;
        this.item = item;
        this.comment = comment;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "id=" + id +
                '}';
    }
}

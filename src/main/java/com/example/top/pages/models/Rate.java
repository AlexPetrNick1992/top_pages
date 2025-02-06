package com.example.top.pages.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="rate")
public class Rate {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
    private String comment;

    @Column(name="ispositive")
    private boolean isPositive;

    @Column(name="isapproved")
    private boolean isApproved;

    @ManyToOne()
    @JoinColumn(name = "item")
    @JsonBackReference
    private Items item;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Rate(Items item, User user, String comment, Boolean isApproved, Boolean isPositive) {
        UUID uuid = UUID.randomUUID();
        this.id = UUID.fromString(uuid.toString());
        this.item = item;
        this.comment = comment;
        this.user = user;
        this.isApproved = isApproved;
        this.isPositive = isPositive;
    }

    public Rate(Items item, UUID id, String comment) {
        this.id = id;
        this.item = item;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "id=" + id +
                '}';
    }
}

package com.example.top.pages.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name="rate")
public class Rate {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
    private String comment;

    @ManyToOne()
    @JoinColumn(name = "item")
    @JsonBackReference
    private Items item;

    public Rate(UUID id, String comment, Items item) {
        this.id = id;
        this.comment = comment;
        this.item = item;
    }
}

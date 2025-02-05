package com.example.top.pages.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Set;
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

    @OneToMany(mappedBy = "item")
    private Set<Rate> rate;

    @ManyToOne
    @JoinColumn(name = "category")
    @JsonBackReference
    private Category category;

}

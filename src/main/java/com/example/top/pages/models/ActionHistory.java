package com.example.top.pages.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="action_history", schema="public")
public class ActionHistory {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "history")
    private String history;

    public ActionHistory(String history, User user) {
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
        this.history = history;
        this.user = user;
    }

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
}

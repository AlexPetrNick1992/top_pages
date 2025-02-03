package com.example.top.pages.repository;

import com.example.top.pages.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Integer> {

    @Query(value = "select * from roles c where id = :id", nativeQuery = true)
    Optional<Roles> findById(int id);

    @Query(value = "select * from roles c where name = :name", nativeQuery = true)
    Roles findByName(String name);
}

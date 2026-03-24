package com.codeMasters.dam1.donaYa.repository;

import com.codeMasters.dam1.donaYa.model.Donante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DonanteAndOrgRepository
    extends JpaRepository<Donante, Long> {
        Optional<Donante> findByEmail(String email);
        boolean existsByEmail(String email);
    }


package com.codeMasters.dam1.donaYa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a una persona física que dona ropa a través de la plataforma.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "donantes")
@DiscriminatorValue("DONANTE")
@PrimaryKeyJoinColumn(name = "id")
public class Donante extends Usuario {

    @Column(name = "total_donaciones", nullable = false)
    private int totalDonaciones = 0;

    @OneToMany(mappedBy = "donante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Donacion> donaciones = new ArrayList<>();

    public Donante(String nombre, String email, String telefono, String direccion) {
        setNombre(nombre);
        setEmail(email);
        setTelefono(telefono);
        setDireccion(direccion);
    }

    /** Incrementa el contador de donaciones realizadas. */
    public void incrementarTotalDonaciones() {
        this.totalDonaciones++;
    }

    @Override
    public String getTipoUsuario() {
        return "DONANTE";
    }
}

